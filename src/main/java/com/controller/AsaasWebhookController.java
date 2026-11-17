
package com.controller;

import com.service.AsaasWebhookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/pagamentos")
public class AsaasWebhookController {

    private final AsaasWebhookService webhookService;
    private final byte[] tokenEsperado;

    public AsaasWebhookController(
            AsaasWebhookService webhookService,
            @Value("${asaas.webhook.token}") String tokenWebhook
    ) {
        if (tokenWebhook == null || tokenWebhook.isBlank()) {
            throw new IllegalStateException(
                    "Token do webhook Asaas não configurado."
            );
        }

        this.webhookService = webhookService;
        this.tokenEsperado =
                tokenWebhook.getBytes(StandardCharsets.UTF_8);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> receberWebhook(
            @RequestHeader(
                    value = "asaas-access-token",
                    required = false
            ) String tokenRecebido,
            @RequestBody String payload
    ) {
        if (tokenRecebido == null ||
                !MessageDigest.isEqual(
                        tokenEsperado,
                        tokenRecebido.getBytes(
                                StandardCharsets.UTF_8
                        )
                )) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Token do webhook inválido."
            );
        }

        webhookService.registrarEvento(payload);

        return ResponseEntity.ok().build();
    }
}