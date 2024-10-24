package br.edu.ibmec.cloud.ecommerce.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ibmec.cloud.ecommerce.entity.Client;
import br.edu.ibmec.cloud.ecommerce.service.ClientService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClienteController {

    @Autowired
    private ClientService service;

    @PostMapping
    public ResponseEntity<Client> create(@RequestBody Client cliente) {
        this.service.save(cliente);
        return new ResponseEntity<>(cliente, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable("id") String id) {
        Optional<Client> cliente = this.service.findById(id);

        if (cliente.isPresent()) {
            return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byCpf/{cpf}")
    public ResponseEntity<Client> getClientByCpf(@PathVariable("cpf") String cpf) {
        Optional<Client> client = this.service.findByCpf(cpf);
        return client.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nome")
    public ResponseEntity<List<Client>> getByNome(@RequestParam String nome) {
        List<Client> clientes = this.service.findByNome(nome);
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/regiao/{regiao}")
    public ResponseEntity<List<Client>> getByRegiao(@PathVariable("regiao") String regiao) {
        List<Client> clientes = this.service.findByRegiao(regiao);
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") String id, @RequestBody Client cliente) {
        try {
            this.service.update(id, cliente);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
