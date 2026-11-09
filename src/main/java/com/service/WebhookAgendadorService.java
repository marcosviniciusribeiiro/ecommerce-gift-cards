package com.service;

import com.model.EventoWebhook;
import com.model.StatusEventoWebhook;
import com.repository.EventoWebhookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(
        prefix = "webhook.processamento",
        name = "habilitado",
        havingValue = "true"
)
public class WebhookAgendadorService {
    private static final Logger logger = LoggerFactory.getLogger(WebhookAgendadorService.class);

    private final EventoWebhookRepository eventoRepository;
    private final WebhookProcessamentoService processamentoService;

    public WebhookAgendadorService(
            EventoWebhookRepository eventoRepository,
            WebhookProcessamentoService processamentoService
    ) {
        this.eventoRepository = eventoRepository;
        this.processamentoService = processamentoService;
    }

    @Scheduled(
            fixedDelayString = "${webhook.processamento.intervalo-ms:60000}",
            initialDelay = 15000
    )
    public void processarEventosPendentes() {
        List<EventoWebhook> eventos = eventoRepository.findByStatus(StatusEventoWebhook.RECEBIDO);

        if (eventos.isEmpty()) return;

        logger.info(
                "Encontrados {} eventos de webhook pendentes.",
                eventos.size()
        );

        for (EventoWebhook evento : eventos) {
            String tipoEvento = evento.getTipoEvento();

            if (!"PAYMENT_RECEIVED".equals(tipoEvento)
                    && !"PAYMENT_CONFIRMED".equals(tipoEvento)) {
                continue;
            }

            try {
                logger.info(
                        "Processando evento Asaas: {}",
                        evento.getIdEventoAsaas()
                );

                processamentoService.processarConfirmacao(
                        evento.getIdEventoAsaas()
                );

                logger.info(
                        "Evento {} processado com sucesso.",
                        evento.getIdEventoAsaas()
                );
            } catch (Exception erro) {
                logger.error(
                        "Falha ao processar evento {}: {}",
                        evento.getIdEventoAsaas(),
                        erro.getMessage(),
                        erro
                );
            }
        }
    }
}