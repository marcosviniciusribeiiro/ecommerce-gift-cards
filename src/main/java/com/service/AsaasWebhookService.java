
package com.service;

import com.dto.asaas.AsaasWebhookRequest;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class AsaasWebhookService {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public AsaasWebhookService(
            JdbcTemplate jdbcTemplate,
            ObjectMapper objectMapper
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void registrarEvento(String payload) {

        AsaasWebhookRequest evento;

        try {
            evento = objectMapper.readValue(
                    payload,
                    AsaasWebhookRequest.class
            );
        } catch (JacksonException erro) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "JSON do webhook inválido.",
                    erro
            );
        }

        if (evento.id() == null || evento.id().isBlank()
                || evento.event() == null
                || evento.event().isBlank()
                || evento.payment() == null
                || evento.payment().id() == null
                || evento.payment().id().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Evento de pagamento incompleto."
            );
        }

        String sql = """
            INSERT INTO tb_eventos_webhook (
                id_evento_asaas,
                tipo_evento,
                id_pagamento_asaas,
                payload,
                status,
                data_recebimento
            )
            VALUES (?, ?, ?, ?, 'RECEBIDO', ?)
            ON DUPLICATE KEY UPDATE
                id_evento_asaas = id_evento_asaas
            """;

        jdbcTemplate.update(
                sql,
                evento.id(),
                evento.event(),
                evento.payment().id(),
                payload,
                LocalDateTime.now()
        );
    }
}