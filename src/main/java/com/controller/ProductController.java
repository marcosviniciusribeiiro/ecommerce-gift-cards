package com.controller;

import com.dto.ProductRequest;
import com.dto.ProductResponse;
import com.model.Product;
import com.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProductResponse> cadastrar(@Valid @RequestBody ProductRequest request){

        Product produto = productService.cadastrar(request);

        ProductResponse response = converterParaResponse(produto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> listarTodos(){
        List<ProductResponse> produtos = productService.listarTodos()
                .stream()
                .map(this::converterParaResponse)
                .toList();

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> buscarPorId(@PathVariable Integer id){
        Product produto = productService.buscarPorId(id);

        return ResponseEntity.ok(
                converterParaResponse(produto)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> atualizarPorId(@PathVariable Integer id,
                                                          @Valid @RequestBody ProductRequest request){
        Product produto = productService.atualizar(id, request);

        return ResponseEntity.ok(
                converterParaResponse(produto)
        );
    }

    private ProductResponse converterParaResponse(Product produto){
        return new ProductResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPlataforma(),
                produto.getValor()
        );
    }
}
