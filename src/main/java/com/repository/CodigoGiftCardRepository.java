package com.repository;

import com.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoGiftCardRepository
        extends JpaRepository<CodigoGiftCard, Integer> {
    Optional<CodigoGiftCard> findFirstByProdutoAndStatus(
            Produto produto,
            StatusCodigo status
    );

    Optional<CodigoGiftCard> findFirstByItemPedido(ItemPedido itemPedido);
}
