package com.repository;

import com.dto.ProdutoMaisVendidoResponse;
import com.model.ItemPedido;
import com.model.Pedido;
import com.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemPedidoRepository
        extends JpaRepository<ItemPedido, Integer> {
    Optional<ItemPedido> findFirstByPedido(Pedido pedido);

    List<ItemPedido> findByPedido(Pedido pedido);

    boolean existsByProduto(Produto produto);

    @Query("""
    SELECT COUNT(i)
    FROM ItemPedido i
    WHERE i.pedido.status = com.model.StatusPedido.Pago
    """)
    Long contarGiftCardsVendidos();

    @Query("""
    SELECT new com.dto.ProdutoMaisVendidoResponse(
        i.produto.id, 
        i.produto.nome, 
        COUNT(i), 
        SUM(i.valorUnitario)
    )
    FROM ItemPedido i
        WHERE i.pedido.status = com.model.StatusPedido.Pago
            GROUP BY i.produto.id, i.produto.nome
                ORDER BY COUNT(i) DESC, i.produto.nome ASC
    """)
    List<ProdutoMaisVendidoResponse> buscarProdutosMaisVendidos();

    @Query("""
    SELECT COUNT(i)
    FROM ItemPedido i
    WHERE i.pedido.status = com.model.StatusPedido.Pago
      AND (:inicio IS NULL OR i.pedido.dataPedido >= :inicio)
      AND (:fimExclusivo IS NULL OR i.pedido.dataPedido < :fimExclusivo)
    """)
    Long contarGiftCardsVendidosPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo
    );

    @Query("""
    SELECT new com.dto.ProdutoMaisVendidoResponse(
        i.produto.id,
        i.produto.nome,
        COUNT(i),
        SUM(i.valorUnitario)    
    )
    FROM ItemPedido i
    WHERE i.pedido.status = com.model.StatusPedido.Pago
      AND (:inicio IS NULL OR i.pedido.dataPedido >= :inicio)
      AND (:fimExclusivo IS NULL OR i.pedido.dataPedido < :fimExclusivo)
    GROUP BY i.produto.id, i.produto.nome
    ORDER BY COUNT(i) DESC, i.produto.nome ASC
    """)
    List<ProdutoMaisVendidoResponse> buscarProdutosMaisVendidosPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo
    );
}