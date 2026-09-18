package com.controller;

import com.dto.CodigoGiftCardRequest;
import com.dto.CodigoGiftCardResponse;
import com.dto.EstoqueProdutoResponse;
import com.model.CodigoGiftCard;
import com.model.StatusCodigo;
import com.service.CodigoGiftCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codigos")
public class CodigoGiftCardController {
    private final CodigoGiftCardService codigoGiftCardService;

    public CodigoGiftCardController(CodigoGiftCardService codigoGiftCardService) {
        this.codigoGiftCardService = codigoGiftCardService;
    }

    @PostMapping
    public ResponseEntity<CodigoGiftCardResponse> cadastrar(@Valid @RequestBody CodigoGiftCardRequest request) {
        CodigoGiftCard codigoGiftCard = codigoGiftCardService.cadastrar(
                request.getIdProduto(),
                request.getCodigo()
        );

        CodigoGiftCardResponse response = converterParaResponse(codigoGiftCard);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<CodigoGiftCardResponse>> listarTodos() {
        List<CodigoGiftCardResponse> codigos = codigoGiftCardService
                .listarTodos()
                .stream()
                .map(this::converterParaResponse)
                .toList();

        return ResponseEntity
                .ok()
                .body(codigos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodigoGiftCardResponse> buscarPorId(@PathVariable Integer id) {
        CodigoGiftCard codigo = codigoGiftCardService.buscarPorId(id);

        return ResponseEntity
                .ok(
                        converterParaResponse(codigo)
                );
    }

    @GetMapping("/produto/{id}")
    public ResponseEntity<List<CodigoGiftCardResponse>> buscarPorProduto(@PathVariable Integer id) {
        List<CodigoGiftCardResponse> codigos = codigoGiftCardService
                .buscarPorIdProduto(id)
                .stream()
                .map(this::converterParaResponse)
                .toList();

        return ResponseEntity.ok(codigos);
    }

    @GetMapping("/produto/{id}/disponiveis")
    public ResponseEntity<List<CodigoGiftCardResponse>> buscarCodigosDisponiveis(@PathVariable Integer id) {
        List<CodigoGiftCardResponse> codigos = codigoGiftCardService.buscarProdutoPeloStatus(id, StatusCodigo.disponivel)
                .stream()
                .map(this::converterParaResponse)
                .toList();

        return ResponseEntity.ok(codigos);
    }

    @GetMapping("/produto/{id}/estoque")
    public ResponseEntity<EstoqueProdutoResponse> consultarEstoqueProduto(@PathVariable Integer id) {
        long quantidade = codigoGiftCardService.contarDisponiveisPorProduto(id);

        return ResponseEntity
                .ok(
                        new EstoqueProdutoResponse(id, quantidade)
                );
    }

    public CodigoGiftCardResponse converterParaResponse(CodigoGiftCard codigo) {
        return new CodigoGiftCardResponse(
                codigo.getId(),
                codigo.getProduto().getId(),
                codigo.getCodigo(),
                codigo.getStatus()
        );
    }
}