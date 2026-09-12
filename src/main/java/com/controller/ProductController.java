package com.controller;

import com.dto.ProductRequest;
import com.dto.ProductResponse;
import com.model.Produto;
import com.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/new")
    public ResponseEntity<ProductResponse> cadastrar(@Valid @RequestBody ProductRequest request){

        Produto produto = service.cadastrar(request);

        ProductResponse response = converterParaResponse(produto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> listarTodos(){
        List<ProductResponse> produtos = service.listarTodos()
                .stream()
                .map(this::converterParaResponse)
                .toList();

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> buscarPorId(@PathVariable Integer id){
        Produto produto = service.buscarPorId(id);

        return ResponseEntity.ok(
                converterParaResponse(produto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> atualizar(@PathVariable Integer id,
                                                          @Valid @RequestBody ProductRequest request){
        Produto produto = service.atualizar(id, request);

        return ResponseEntity.ok(
                converterParaResponse(produto)
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id){
        service.deletar(id);

        return ResponseEntity.noContent().build();
    }

    private ProductResponse converterParaResponse(Produto produto){
        return new ProductResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPlataforma(),
                produto.getValor()
        );
    }
}
