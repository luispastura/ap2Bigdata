package br.edu.ibmec.cloud.ecommerce.entity;
import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "clients")
public class Client {

    @Id
    private String clienteId;

    private String nome;

    @NotNull
    private String cpf;
    
    private String email;
    private String cartaoDeCredito;

    @PartitionKey
    private String regiao;
}
