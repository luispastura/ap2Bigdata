package br.edu.ibmec.cloud.ecommerce.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ibmec.cloud.ecommerce.entity.Product;
import br.edu.ibmec.cloud.ecommerce.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        this.service.save(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") String id) {
        Optional<Product> product = this.service.findById(id, id);

        if (product.isPresent()) {
            return new ResponseEntity<>(product.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("category") String category) {
        List<Product> products = this.service.findByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getByProductName(@RequestParam String productName) {
        List<Product> products = this.service.findProductByName(productName);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") String id, @RequestBody Product product) {
        try {
            this.service.update(id, product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        try {
            this.service.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
