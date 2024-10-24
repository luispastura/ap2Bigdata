package br.edu.ibmec.cloud.ecommerce.repository;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import br.edu.ibmec.cloud.ecommerce.entity.Compra;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompraRepository extends CosmosRepository<Compra, String> {
    List<Compra> findByClienteId(String clienteId);
    List<Compra> findByRegiao(String regiao);
    List<Compra> findByDataCompraBetween(LocalDateTime inicio, LocalDateTime fim);
}
