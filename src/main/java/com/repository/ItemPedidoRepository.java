package com.repository;

import com.model.ItemPedido;
import com.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemPedidoRepository
        extends JpaRepository<ItemPedido, Integer> {

    Optional<ItemPedido> findFirstByPedido(Pedido pedido);

}
