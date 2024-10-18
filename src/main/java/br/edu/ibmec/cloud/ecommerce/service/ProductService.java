package br.edu.ibmec.cloud.ecommerce.service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.azure.cosmos.models.PartitionKey;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findProductByName(String productName) {
        return this.productRepository.findByProductName(productName);
    }

    public Optional<Product> findById(String productId, String id) {
        return this.productRepository.findById(productId);
    }

    public void save(Product product) {
        product.setProductId(UUID.randomUUID().toString());
        this.productRepository.save(product);
    }

    public List<Product> findByCategory(String category) {
        return this.productRepository.findByProductCategory(category);
    }

    public void delete(String productId) throws Exception {
        Optional<Product> optProduct = this.productRepository.findById(productId);
    
        if (optProduct.isEmpty()) {
            throw new Exception("Produto não encontrado para exclusão");
        }
    
        // Obtém a categoria do produto para usar como PartitionKey
        Product product = optProduct.get();
        this.productRepository.deleteById(productId, new PartitionKey(product.getProductCategory()));
    }
    

    public void update(String productId, Product productAtualizado) throws Exception {
        Optional<Product> optProduct = this.productRepository.findById(productId);
    
        if (!optProduct.isPresent()) {
            throw new Exception("Produto não encontrado para atualização");
        }
    
        Product produtoExistente = optProduct.get();
    
        // Mantém o mesmo ID do produto existente
        productAtualizado.setProductId(produtoExistente.getProductId());
    
        try {
            // Salva o produto atualizado, possivelmente com nova categoria
            this.productRepository.save(productAtualizado);
    
            // Se a categoria mudou, é necessário deletar o produto antigo
            if (!produtoExistente.getProductCategory().equals(productAtualizado.getProductCategory())) {
                this.productRepository.deleteById(productId, new PartitionKey(produtoExistente.getProductCategory()));
            }
        } catch (Exception e) {
            throw new Exception("Erro ao atualizar o produto: " + e.getMessage());
        }
    }
}    
