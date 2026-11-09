package com.service;

import com.dto.asaas.CobrancaAsaasResponse;
import com.model.*;
import com.repository.EventoWebhookRepository;
import com.repository.PagamentoRepository;
import com.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ConfirmacaoAutomaticaService {
    private final EventoWebhookRepository eventoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;

    public ConfirmacaoAutomaticaService(
            EventoWebhookRepository eventoRepository,
            PagamentoRepository pagamentoRepository,
            PedidoRepository pedidoRepository,
            PedidoService pedidoService
    ) {
        this.eventoRepository = eventoRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoService = pedidoService;
    }

    @Transactional
    public void concluirConfirmacao(
            String idEvento,
            CobrancaAsaasResponse cobranca
    ) {
        EventoWebhook evento = eventoRepository.buscarPorIdComBloqueio(idEvento)
                .orElseThrow(
                        () -> new IllegalArgumentException("Evento não encontrado.")
                );

        if (evento.getStatus() == StatusEventoWebhook.PROCESSADO
                | evento.getStatus() == StatusEventoWebhook.REQUER_ATENCAO) {
            return;
        }

        if (!"PAYMENT_RECEIVED".equals(evento.getTipoEvento())
                && !"PAYMENT_CONFIRMED".equals(evento.getTipoEvento())) {
            throw new IllegalStateException("Evento não representa confirmação de pagamento.");
        }

        if (cobranca == null
                || !evento.getIdPagamentoAsaas()
                .equals(cobranca.id())) {
            throw new IllegalStateException(
                    "Cobrança incompatível com o evento."
            );
        }

        // Localizar o pagamento
        Pagamento pagamento = pagamentoRepository.findByIdPagamentoAsaas(cobranca.id())
                .orElseThrow(
                        () -> new IllegalArgumentException("Pagamento não encontrado.")
                );

        Integer idPedido = pagamento.getPedido().getId();

        Pedido pedido = pedidoRepository.buscarPorIdComBloqueio(idPedido)
                .orElseThrow(
                        () -> new IllegalArgumentException("Pedido não encontrado.")
                );

        if (!("PEDIDO-" + idPedido).equals(cobranca.externalReference())) {
            throw new IllegalStateException("Referência externa incompatível.");
        }

        if (cobranca.value() == null
                || pagamento.getValor()
                .compareTo(cobranca.value()) != 0
                || pedido.getValorTotal()
                .compareTo(cobranca.value()) != 0) {
            throw new IllegalStateException("Valores da cobrança e do pedido divergentes.");
        }

        if (pagamento.getFormaPagamento() != FormaPagamento.PIX
                || !"PIX".equals(cobranca.billingType())) {
            throw new IllegalStateException("Forma de pagamento incompatível.");
        }

        // Verificar o status informado pelo Asaas
        StatusPagamento novoStatus;

        if ("RECEIVED".equals(cobranca.status())) {
            novoStatus = StatusPagamento.RECEIVED;
        } else if ("CONFIRMED".equals(cobranca.status())) {
            novoStatus = StatusPagamento.CONFIRMED;
        } else {
            throw new IllegalStateException("A cobrança ainda não foi confirmada.");
        }

        if (pedido.getStatus() == StatusPedido.Cancelado) {
            if (pagamento.getStatus() != StatusPagamento.RECEIVED) {
                pagamento.setStatus(novoStatus);
            }

            if (pagamento.getDataConfirmacao() == null) {
                pagamento.setDataConfirmacao(LocalDateTime.now());
            }

            pagamentoRepository.save(pagamento);

            evento.setStatus(StatusEventoWebhook.REQUER_ATENCAO);
            evento.setDataProcessamento(LocalDateTime.now());

            return;
        }

        if (pedido.getStatus() == StatusPedido.Pendente) {
            pedidoService.confirmarPedido(idPedido);
        }

        if (pagamento.getStatus() != StatusPagamento.RECEIVED) {
            pagamento.setStatus(novoStatus);
        }

        if (pagamento.getDataConfirmacao() == null) {
            pagamento.setDataConfirmacao(LocalDateTime.now());
        }

        pagamentoRepository.save(pagamento);

        evento.marcarComoProcessado();
        eventoRepository.save(evento);
    }
}
