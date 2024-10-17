package br.edu.ibmec.cloud.ecommerce.service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.azure.cosmos.implementation.guava25.base.Optional;
import br.edu.ibmec.cloud.ecommerce.entity.Compra;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.repository.ClientRepository;
import br.edu.ibmec.cloud.ecommerce.repository.CompraRepository;
import br.edu.ibmec.cloud.ecommerce.repository.ProductRepository;
import ch.qos.logback.core.net.server.Client;

@Service
public class CompraService {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ClientRepository clienteRepository;

    @Autowired
    private ProductRepository productRepository;

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
        compra.setRegiao(((Compra) cliente).getRegiao());

        // Salvar a compra no CosmosDB
        compraRepository.save(compra);

        // Aqui você pode adicionar lógica para processar o pagamento usando o cartão de crédito
        // Certifique-se de tratar informações sensíveis de forma segura
    }

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
