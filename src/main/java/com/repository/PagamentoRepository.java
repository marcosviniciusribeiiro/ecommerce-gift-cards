package com.repository;

import com.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    Optional<Pagamento> findByIdPagamentoAsaas(String idPagamentoAsaas);

    List<Pagamento> findByPedidoId(Integer idPedido);
}
