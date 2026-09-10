package com.controller;

import com.dto.ProductRequest;
import com.model.Product;
import com.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> cadastrar(@Valid @RequestBody ProductRequest request){

        Product produto = productService.cadastrar(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produto);
    }
}
