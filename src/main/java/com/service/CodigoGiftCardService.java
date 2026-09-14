package com.service;

import com.exception.CodeNotFoundException;
import com.exception.ProductNotFoundException;
import com.model.CodigoGiftCard;
import com.model.ItemPedido;
import com.model.Produto;
import com.model.StatusCodigo;
import com.repository.CodigoGiftCardRepository;
import com.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodigoGiftCardService {
    private final CodigoGiftCardRepository codigoGiftCardRepository;
    private final ProdutoRepository produtoRepository;

    public CodigoGiftCardService(
            CodigoGiftCardRepository codigoGiftCardRepository,
            ProdutoRepository produtoRepository
    ) {
        this.codigoGiftCardRepository = codigoGiftCardRepository;
        this.produtoRepository = produtoRepository;
    }

    public CodigoGiftCard cadastrar(
            Integer idProduto,
            String codigo
    ){
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() ->
                        new ProductNotFoundException("Produto não encontrado.")
                );

        CodigoGiftCard codigoGiftCard = new CodigoGiftCard();
        codigoGiftCard.setProduto(produto);
        codigoGiftCard.setCodigo(codigo);
        codigoGiftCard.setStatus(StatusCodigo.disponivel);
        codigoGiftCard.setItemPedido(null);

        return codigoGiftCardRepository.save(codigoGiftCard);
    }

    public CodigoGiftCard atribuirCodigo(
            Produto produto,
            ItemPedido itemPedido
    ) {
        CodigoGiftCard codigoGiftCard = codigoGiftCardRepository.findFirstByProdutoAndStatus(
                produto,
                StatusCodigo.disponivel
        ).orElseThrow(() ->
                new CodeNotFoundException("Não há códigos disponíveis para esse produto."));

        codigoGiftCard.setItemPedido(itemPedido);
        codigoGiftCard.setStatus(StatusCodigo.vendido);

        return codigoGiftCardRepository.save(codigoGiftCard);
    }

    public List<CodigoGiftCard> listarTodos() {
        return codigoGiftCardRepository.findAll();
    }

    public CodigoGiftCard buscarPorId(Integer id){
        return codigoGiftCardRepository
                .findById(id)
                .orElseThrow(() -> new CodeNotFoundException("Código não encontrado."));
    }

    public List<CodigoGiftCard> buscarPorIdProduto(Integer idProduto) {
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() ->
                        new ProductNotFoundException("Produto não encontrado.")
                );

        return codigoGiftCardRepository.findByProduto(produto);
    }

    public List<CodigoGiftCard> buscarProdutoPeloStatus(
            Integer idProduto,
            StatusCodigo status
    ){
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() ->
                        new ProductNotFoundException("Produto não encontrado."));

        return codigoGiftCardRepository.findByProdutoAndStatus(produto, status);
    }

    public long contarDisponiveisPorProduto(Integer idProduto){
        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() ->
                        new ProductNotFoundException("Produto não encontrado."));

        return codigoGiftCardRepository.countByProdutoAndStatus(produto, StatusCodigo.disponivel);
    }
}
