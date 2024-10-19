package br.edu.ibmec.cloud.ecommerce.service;
import java.time.LocalDateTime;

import java.util.*;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ibmec.cloud.ecommerce.entity.Client;
import br.edu.ibmec.cloud.ecommerce.entity.Compra;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.repository.ClientRepository;
import br.edu.ibmec.cloud.ecommerce.repository.CompraRepository;
import br.edu.ibmec.cloud.ecommerce.repository.ProductRepository;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ClientRepository clienteRepository;

    @Autowired
    private ProductRepository productRepository;

    // Método para processar compras
    public void processarCompra(String clienteId, List<String> produtosIds) throws Exception {
        // Verificar se o cliente existe
        java.util.Optional<br.edu.ibmec.cloud.ecommerce.entity.Client> optCliente = clienteRepository.findById(clienteId);
        if (!optCliente.isPresent()) {
            throw new Exception("Cliente não encontrado");
        }


        Client cliente = (Client) optCliente.get();

        // Verificar se os produtos existem e calcular o valor total
        double valorTotal = 0.0;
        for (String produtoId : produtosIds) {
            java.util.Optional<Product> optProduto = productRepository.findById(produtoId);
            if (!optProduto.isPresent()) {
                throw new Exception("Produto com ID " + produtoId + " não encontrado");
            }
            valorTotal += optProduto.get().getPrice();
        }

        // Criar a compra
        Compra compra = new Compra();
        compra.setCompraId(UUID.randomUUID().toString());
        compra.setClienteId(clienteId);
        compra.setProdutosIds(produtosIds);
        compra.setValorTotal(valorTotal);
        compra.setDataCompra(LocalDateTime.now());


        // Definir a região do cliente (corrigir a lógica de como obter a região)

        // compra.setRegiao(((Compra) cliente).getRegiao()); tirar do comentario

        // Salvar a compra no CosmosDB
        compraRepository.save(compra);
    }

    // Novo método para calcular o ticket médio das vendas
    public double calcularTicketMedio(LocalDateTime inicio, LocalDateTime fim) {
        List<Compra> compras = compraRepository.findByDataCompraBetween(inicio, fim);
        if (compras.isEmpty()) {
            return 0.0; // Evitar divisão por zero
        }
        double valorTotal = compras.stream().mapToDouble(Compra::getValorTotal).sum();
        return valorTotal / compras.size();
    }

    // Novo método para obter produtos mais vendidos por região
    public Map<String, Long> obterProdutosMaisVendidosPorRegiao(String regiao) {
        List<Compra> compras = compraRepository.findByRegiao(regiao);
        Map<String, Long> produtosVendidos = new HashMap<>();

        for (Compra compra : compras) {
            for (String produtoId : compra.getProdutosIds()) {
                produtosVendidos.put(produtoId, produtosVendidos.getOrDefault(produtoId, 0L) + 1);
            }
        }
        return produtosVendidos;
    }

    // Métodos existentes
    public List<Compra> obterComprasPorCliente(String clienteId) {
        return compraRepository.findByClienteId(clienteId);
    }

    public List<Compra> obterComprasPorRegiao(String regiao) {
        return compraRepository.findByRegiao(regiao);
    }

    public List<Compra> obterComprasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return compraRepository.findByDataCompraBetween(inicio, fim);
    }
}

