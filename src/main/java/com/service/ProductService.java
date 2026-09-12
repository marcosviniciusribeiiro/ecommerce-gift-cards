package com.service;

import com.dto.ProductRequest;
import com.exception.ProductNotFoundException;
import com.model.Produto;
import com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Produto cadastrar(ProductRequest request){

        Produto produto = new Produto();

        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPlataforma(request.getPlataforma());
        produto.setValor(request.getValor());

        return repository.save(produto);
    }

    public List<Produto> listarTodos(){
        return repository.findAll();
    }

    public Produto buscarPorId(Integer id){
        return repository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado."));
    }

    public Produto atualizar(Integer id, ProductRequest request){
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produto não encontrado."));

        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPlataforma(request.getPlataforma());
        produto.setValor(request.getValor());

        return repository.save(produto);
    }

    public void deletar(Integer id){
        Produto produto = repository.findById(id)
                        .orElseThrow(() ->
                                new ProductNotFoundException("Produto não encontrado.")
                        );

        repository.delete(produto);
    }
}
