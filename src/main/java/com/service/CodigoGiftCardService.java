package com.service;

import com.exception.CodeNotFoundException;
import com.model.CodigoGiftCard;
import com.model.ItemPedido;
import com.model.Produto;
import com.model.StatusCodigo;
import com.repository.CodigoGiftCardRepository;
import org.springframework.stereotype.Service;

@Service
public class CodigoGiftCardService {
    private final CodigoGiftCardRepository repository;

    public CodigoGiftCardService(CodigoGiftCardRepository repository) {
        this.repository = repository;
    }

    public CodigoGiftCard atribuirCodigo(
            Produto produto,
            ItemPedido itemPedido) {
        CodigoGiftCard codigoGiftCard = repository.findFirstByProdutoAndStatus(
                produto,
                StatusCodigo.disponivel
        ).orElseThrow(() ->
                new CodeNotFoundException("Não há códigos disponíveis para esse produto."));

        codigoGiftCard.setItemPedido(itemPedido);
        codigoGiftCard.setStatus(StatusCodigo.vendido);

        return repository.save(codigoGiftCard);
    }
}
