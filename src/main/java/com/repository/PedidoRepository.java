package com.repository;

import com.model.Pedido;
import com.model.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository
        extends JpaRepository<Pedido, Integer> {
    boolean existsByUsuario(Usuario usuario);

    List<Pedido> findByUsuario(Usuario usuario);

    Optional<Pedido> findByIdAndUsuario(Integer id, Usuario usuario);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pedido p WHERE p.id = :id")
    Optional<Pedido> buscarPorIdComBloqueio(
            @Param("id") Integer id
    );

    @Query("""
    SELECT COUNT(p)
    FROM Pedido p
    WHERE p.status = com.model.StatusPedido.Pago
    """)
    Long contarPedidosPagos();

    @Query("""
    SELECT COALESCE(SUM(p.valorTotal), 0)
    FROM Pedido p
    WHERE p.status = com.model.StatusPedido.Pago
    """)
    BigDecimal calcularFaturamentoTotal();

    @Query("""
    SELECT COUNT(p)
    FROM Pedido p
    WHERE p.status = com.model.StatusPedido.Pago
      AND (:inicio IS NULL OR p.dataPedido >= :inicio)
      AND (:fimExclusivo IS NULL OR p.dataPedido < :fimExclusivo)
    """)
    Long contarPedidosPagosPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo
    );

    @Query("""
    SELECT COALESCE(SUM(p.valorTotal), 0)
    FROM Pedido p
    WHERE p.status = com.model.StatusPedido.Pago
      AND (:inicio IS NULL OR p.dataPedido >= :inicio)
      AND (:fimExclusivo IS NULL OR p.dataPedido < :fimExclusivo)
    """)
    BigDecimal calcularFaturamentoPorPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fimExclusivo") LocalDateTime fimExclusivo
    );
}
