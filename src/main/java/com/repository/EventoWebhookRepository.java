package com.repository;

import com.model.EventoWebhook;
import com.model.StatusEventoWebhook;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventoWebhookRepository extends JpaRepository<EventoWebhook, String> {
    List<EventoWebhook> findByStatus(StatusEventoWebhook status);

    List<EventoWebhook> findByIdPagamentoAsaas(String idPagamentoAsaas);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT e FROM EventoWebhook e
    WHERE e.idEventoAsaas = :id
""")
    Optional<EventoWebhook> buscarPorIdComBloqueio(
            @Param("id") String id
    );
}
