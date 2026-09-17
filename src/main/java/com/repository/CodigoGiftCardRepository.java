package com.repository;

import com.model.CodigoGiftCard;
import com.model.ItemPedido;
import com.model.Produto;
import com.model.StatusCodigo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CodigoGiftCardRepository
        extends JpaRepository<CodigoGiftCard, Integer> {

    Optional<CodigoGiftCard> findFirstByProdutoAndStatus(Produto produto, StatusCodigo status);

    Optional<CodigoGiftCard> findFirstByItemPedido(ItemPedido itemPedido);

    List<CodigoGiftCard> findByProduto(Produto produto);

    List<CodigoGiftCard> findByProdutoAndStatus(Produto produto, StatusCodigo status);

    long countByProdutoAndStatus(Produto produto, StatusCodigo status);
}