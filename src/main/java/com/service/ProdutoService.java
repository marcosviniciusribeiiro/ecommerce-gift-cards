package com.service;

import com.dto.ProdutoRequest;
import com.exception.ProductNotFoundException;
import com.model.Produto;
import com.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastrar(ProdutoRequest request) {
        Produto produto = new Produto();
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPlataforma(request.getPlataforma());
        produto.setValor(request.getValor());

        return produtoRepository.save(produto);
    }

    public List<Produto> listarTodos(){
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado.")
                );
    }

    public Produto atualizar(
            Integer id,
            ProdutoRequest request) {
        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado.")
                );

        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPlataforma(request.getPlataforma());
        produto.setValor(request.getValor());

        return produtoRepository.save(produto);
    }

    public void deletar(Integer id) {
        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado.")
                );

        produtoRepository.delete(produto);
    }
}