package com.controller;

import com.service.WebhookProcessamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/webhooks")
public class WebhookProcessamentoController {
    private final WebhookProcessamentoService processamentoService;

    public WebhookProcessamentoController(WebhookProcessamentoService processamentoService) {
        this.processamentoService = processamentoService;
    }

    @PostMapping("/{idEvento}/validar-pagamentos")
    public ResponseEntity<Void> validarPagamento(@PathVariable String idEvento) {
        processamentoService.atualizarPagamento(idEvento);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idEvento}/processar")
    public ResponseEntity<Void> processarEvento(@PathVariable String idEvento) {
        processamentoService.processarConfirmacao(idEvento);

        return ResponseEntity.noContent().build();
    }
}
