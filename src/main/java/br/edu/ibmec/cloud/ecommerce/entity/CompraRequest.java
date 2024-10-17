package br.edu.ibmec.cloud.ecommerce.entity;
import lombok.Data;
import java.util.List;

@Data
public class CompraRequest {
    private String clienteId;
    private List<String> produtosIds;
}