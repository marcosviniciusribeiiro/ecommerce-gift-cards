package com.service;

import com.dto.ProdutoRequest;
import com.exception.ProductNotFoundException;
import com.model.Produto;
import com.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto cadastrar(ProdutoRequest request){

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

    public Produto atualizar(Integer id, ProdutoRequest request){
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
