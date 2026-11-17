package com.service;

import com.dto.ProdutoRequest;
import com.exception.ProductNotFoundException;
import com.exception.ProdutoEmUsoException;
import com.model.Produto;
import com.repository.CodigoGiftCardRepository;
import com.repository.ItemPedidoRepository;
import com.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final CodigoGiftCardRepository codigoGiftCardRepository;

    public ProdutoService(ProdutoRepository produtoRepository,
                          ItemPedidoRepository itemPedidoRepository,
                          CodigoGiftCardRepository codigoGiftCardRepository) {
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.codigoGiftCardRepository = codigoGiftCardRepository;
    }

    @Transactional
    public Produto cadastrar(ProdutoRequest request) {
        Produto produto = new Produto();

        preencherDados(produto, request);

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

    @Transactional
    public Produto atualizar(
            Integer id,
            ProdutoRequest request) {
        Produto produto = buscarPorId(id);

        preencherDados(produto, request);

        return produtoRepository.save(produto);
    }

    public void deletar(Integer id) {
        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado.")
                );

        if (itemPedidoRepository.existsByProduto(produto)
                || codigoGiftCardRepository.existsByProduto(produto)){
            throw new ProdutoEmUsoException("Não é possível excluir um produto com pedidos ou códigos associados.");
        }
        produtoRepository.delete(produto);
    }

    private void preencherDados(
            Produto produto,
            ProdutoRequest request
    ) {
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPlataforma(request.getPlataforma());
        produto.setValor(request.getValor());
    }
}