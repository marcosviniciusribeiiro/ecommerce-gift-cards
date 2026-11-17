package com.controller;

import com.dto.PagamentoCriadoResponse;
import com.dto.asaas.PixQrCodeResponse;
import com.service.PagamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {
    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/pedido/{idPedido}/pix")
    public ResponseEntity<PagamentoCriadoResponse> criarPagamentoPix(
            @PathVariable Integer idPedido,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();

        PagamentoCriadoResponse resposta = pagamentoService.criarPagamentoPix(
                idPedido,
                emailUsuario
        );

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{idPagamento}/pix/qrcode")
    public ResponseEntity<PixQrCodeResponse> consultarQrCodePix(
            @PathVariable Integer idPagamento,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();

        PixQrCodeResponse resposta = pagamentoService.consultarQrCodePix(
                idPagamento,
                emailUsuario
        );

        return ResponseEntity.ok(resposta);
    }
}