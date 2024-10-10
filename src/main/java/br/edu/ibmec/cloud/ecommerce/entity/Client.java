package br.edu.ibmec.cloud.ecommerce.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;

import lombok.Data;
import nonapi.io.github.classgraph.json.Id;

@Data
@Container(containerName = "clients")
public class Client {

    @Id
    private String clienteId;

    private String nome;
    private String email;
    private String cartaoDeCredito;

    @PartitionKey
    private String regiao;
}
