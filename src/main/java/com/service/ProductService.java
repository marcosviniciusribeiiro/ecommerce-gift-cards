package com.service;

import com.dto.ProductRequest;
import com.model.Product;
import com.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product cadastrar(ProductRequest request){
        Product produto = new Product();

        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPlataforma(request.getPlataforma());
        produto.setValor(request.getValor());

        return productRepository.save(produto);
    }
}
