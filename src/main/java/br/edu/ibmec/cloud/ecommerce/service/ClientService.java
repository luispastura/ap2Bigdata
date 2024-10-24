package br.edu.ibmec.cloud.ecommerce.service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.azure.cosmos.models.PartitionKey;
import br.edu.ibmec.cloud.ecommerce.entity.Client;
import br.edu.ibmec.cloud.ecommerce.repository.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clienteRepository;

    public void save(Client cliente) {
        // Verifica se já existe um cliente com o mesmo CPF
        Optional<Client> existingClientByCpf = clienteRepository.findByCpf(cliente.getCpf());
        if (existingClientByCpf.isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
    
        // Verifica se já existe um cliente com o mesmo e-mail
        List<Client> existingClientByEmail = clienteRepository.findByEmail(cliente.getEmail());
        if (!existingClientByEmail.isEmpty()) { // Verifica se a lista não está vazia
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
    
        // Gera um novo ID para o cliente
        cliente.setClienteId(UUID.randomUUID().toString());
        // Salva o cliente no banco de dados
        this.clienteRepository.save(cliente);
    }
    
    public java.util.Optional<Client> findById(String clienteId) {
        return this.clienteRepository.findById(clienteId);
    }

    public List<Client> findByNome(String nome) {
        return this.clienteRepository.findByNome(nome);
    }

    public List<Client> findByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Optional<Client> findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }

    public List<Client> findByRegiao(String regiao) {
        return this.clienteRepository.findByRegiao(regiao);
    }

    public void delete(String clienteId) throws Exception {
        java.util.Optional<Client> optCliente = this.clienteRepository.findById(clienteId);

        if (!optCliente.isPresent()) {
            throw new Exception("Cliente não encontrado para exclusão");
        }

        this.clienteRepository.deleteById(clienteId, new PartitionKey(optCliente.get().getRegiao()));
    }

    public void update(String clienteId, Client clienteAtualizado) throws Exception {
        java.util.Optional<Client> optCliente = this.clienteRepository.findById(clienteId);
    
        if (!optCliente.isPresent()) {
            throw new Exception("Cliente não encontrado para atualização");
        }
    
        Client clienteExistente = optCliente.get();
        
        // Tente salvar o cliente atualizado (com a nova região) antes de excluir o cliente antigo
        clienteAtualizado.setClienteId(clienteExistente.getClienteId());  // Garanta que o ID permaneça o mesmo
        
        try {
            // Salva o cliente atualizado com a nova partição (nova região)
            this.clienteRepository.save(clienteAtualizado);
            
            // Após salvar o cliente atualizado, delete o cliente antigo
            this.clienteRepository.deleteById(clienteId, new PartitionKey(clienteExistente.getRegiao()));
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar o cliente: " + e.getMessage());
        }
    }
    
    
    
}
