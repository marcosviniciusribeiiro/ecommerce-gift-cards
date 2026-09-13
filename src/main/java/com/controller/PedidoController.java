package com.controller;

import com.dto.PedidoConfirmadoResponse;
import com.dto.PedidoRequest;
import com.dto.PedidoResponse;
import com.model.Pedido;
import com.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(
            @Valid @RequestBody PedidoRequest request,
            Authentication authentication
    ) {
        String emailUsuario = authentication.getName();

        Pedido pedido = service.criarPedido(
                emailUsuario,
                request.getIdProduto()
        );

        PedidoResponse response = new PedidoResponse(
                pedido.getId(),
                request.getIdProduto(),
                pedido.getStatus(),
                pedido.getDataPedido(),
                pedido.getValorTotal()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<PedidoConfirmadoResponse> confirmarPedido(
            @PathVariable Integer id
    ){
        PedidoConfirmadoResponse response = service
                .confirmarPedido(id);

        return ResponseEntity.ok(response);
    }
}
