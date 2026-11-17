package com.service;

import com.exception.CodeConflictException;
import com.exception.CodeNotFoundException;
import com.exception.ProductNotFoundException;
import com.model.CodigoGiftCard;
import com.model.ItemPedido;
import com.model.Produto;
import com.model.StatusCodigo;
import com.repository.CodigoGiftCardRepository;
import com.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CodigoGiftCardService {
    private final CodigoGiftCardRepository codigoGiftCardRepository;
    private final ProdutoRepository produtoRepository;

    public CodigoGiftCardService(
            CodigoGiftCardRepository codigoGiftCardRepository,
            ProdutoRepository produtoRepository) {
        this.codigoGiftCardRepository = codigoGiftCardRepository;
        this.produtoRepository = produtoRepository;
    }

    public CodigoGiftCard cadastrar(
            Integer id,
            String codigo) {
        Produto produto = buscarProduto(id);

        if (codigoGiftCardRepository.existsByCodigo(codigo)) {
            throw new CodeConflictException(
                    "Este código de gift card já está cadastrado."
            );
        }

        CodigoGiftCard codigoGiftCard = new CodigoGiftCard();
        codigoGiftCard.setProduto(produto);
        codigoGiftCard.setCodigo(codigo);
        codigoGiftCard.setStatus(StatusCodigo.Disponivel);
        codigoGiftCard.setItemPedido(null);

        return codigoGiftCardRepository.save(codigoGiftCard);
    }

    public CodigoGiftCard atribuirCodigo(
            Produto produto,
            ItemPedido item) {
        CodigoGiftCard codigoGiftCard = codigoGiftCardRepository
                .findFirstByProdutoAndStatusOrderByIdAsc(produto, StatusCodigo.Disponivel)
                .orElseThrow(
                        () -> new CodeNotFoundException("Não há códigos disponíveis para esse produto.")
                );

        codigoGiftCard.setItemPedido(item);
        codigoGiftCard.setStatus(StatusCodigo.Vendido);

        return codigoGiftCardRepository.save(codigoGiftCard);
    }

    public List<CodigoGiftCard> listarTodos() {
        return codigoGiftCardRepository.findAll();
    }

    public CodigoGiftCard buscarPorId(Integer id) {
        return codigoGiftCardRepository
                .findById(id)
                .orElseThrow(
                        () -> new CodeNotFoundException("Código não encontrado.")
                );
    }

    public List<CodigoGiftCard> buscarPorIdProduto(Integer id) {
        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado.")
                );

        return codigoGiftCardRepository.findByProduto(produto);
    }

    public List<CodigoGiftCard> buscarProdutoPeloStatus(
            Integer id,
            StatusCodigo status){
        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado.")
                );

        return codigoGiftCardRepository.findByProdutoAndStatus(produto, status);
    }

    public CodigoGiftCard buscarPorItemPedido(ItemPedido item){
        return codigoGiftCardRepository
                .findFirstByItemPedido(item)
                .orElseThrow(
                        () -> new CodeNotFoundException("Código do pedido não encontrado.")
                );
    }

    public long contarDisponiveisPorProduto(Integer id){
        Produto produto = produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException("Produto não encontrado.")
                );

        return codigoGiftCardRepository.countByProdutoAndStatus(produto, StatusCodigo.Disponivel);
    }

    @Transactional
    public CodigoGiftCard atualizar(
            Integer id,
            String novoCodigo) {

        CodigoGiftCard codigoGiftCard = buscarPorId(id);

        validarCodigoDisponivel(codigoGiftCard);


        if (!codigoGiftCard.getCodigo().equals(novoCodigo) && codigoGiftCardRepository.existsByCodigo(novoCodigo)) {
            throw new CodeConflictException("Este código de gift card já está cadastrado.");
        }

        codigoGiftCard.setCodigo(novoCodigo);

        return codigoGiftCardRepository.save(codigoGiftCard);
    }

    @Transactional
    public void deletar(Integer id) {
        CodigoGiftCard codigoGiftCard = buscarPorId(id);

        validarCodigoDisponivel(codigoGiftCard);

        codigoGiftCardRepository.deleteById(id);
    }

    private Produto buscarProduto(Integer id) {
        return produtoRepository
                .findById(id)
                .orElseThrow(
                        () -> new ProductNotFoundException(
                                "Produto não encontrado."
                        )
                );
    }

    private void validarCodigoDisponivel(CodigoGiftCard codigo) {
        if (codigo.getStatus() != StatusCodigo.Disponivel
                || codigo.getItemPedido() != null) {

            throw new CodeConflictException(
                    "Não é possível modificar um código vendido ou associado a um pedido."
            );
        }
    }

}