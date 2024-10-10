package br.edu.ibmec.cloud.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ibmec.cloud.ecommerce.entity.Compra;
import br.edu.ibmec.cloud.ecommerce.entity.CompraRequest;
import br.edu.ibmec.cloud.ecommerce.service.CompraService;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    public ResponseEntity<String> processarCompra(@RequestBody CompraRequest compraRequest) {
        try {
            compraService.processarCompra(compraRequest.getClienteId(), compraRequest.getProdutosIds());
            return new ResponseEntity<>("Compra processada com sucesso", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao processar compra: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Compra>> obterComprasPorCliente(@PathVariable String clienteId) {
        List<Compra> compras = compraService.obterComprasPorCliente(clienteId);
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @GetMapping("/regiao/{regiao}")
    public ResponseEntity<List<Compra>> obterComprasPorRegiao(@PathVariable String regiao) {
        List<Compra> compras = compraService.obterComprasPorRegiao(regiao);
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<Compra>> obterComprasPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim) {

        LocalDateTime inicio = LocalDateTime.parse(dataInicio);
        LocalDateTime fim = LocalDateTime.parse(dataFim);

        List<Compra> compras = compraService.obterComprasPorPeriodo(inicio, fim);
        return new ResponseEntity<>(compras, HttpStatus.OK);
    }
}
