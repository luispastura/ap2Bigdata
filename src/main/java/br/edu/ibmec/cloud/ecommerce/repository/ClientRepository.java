package br.edu.ibmec.cloud.ecommerce.repository;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import br.edu.ibmec.cloud.ecommerce.entity.Client;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends CosmosRepository<Client, String> {
    List<Client> findByNome(String nome);
    Optional<Client> findByCpf(String cpf);
    List<Client> findByRegiao(String regiao);
    List<Client> findByEmail(String email);
}
