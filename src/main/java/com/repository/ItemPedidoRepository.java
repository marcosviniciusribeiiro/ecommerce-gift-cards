package com.repository;

import com.model.ItemPedido;
import com.model.Pedido;
import com.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemPedidoRepository
        extends JpaRepository<ItemPedido, Integer> {
    Optional<ItemPedido> findFirstByPedido(Pedido pedido);

    List<ItemPedido> findByPedido(Pedido pedido);

    boolean existsByProduto(Produto produto);
}
