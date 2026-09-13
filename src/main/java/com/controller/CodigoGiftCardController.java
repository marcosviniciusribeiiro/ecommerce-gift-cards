package com.controller;

import com.dto.CodigoGiftCardRequest;
import com.dto.CodigoGiftCardResponse;
import com.model.CodigoGiftCard;
import com.service.CodigoGiftCardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/codigos")
public class CodigoGiftCardController {
    private final CodigoGiftCardService service;

    public CodigoGiftCardController(CodigoGiftCardService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CodigoGiftCardResponse> cadastrar(
            @Valid @RequestBody CodigoGiftCardRequest request) {
        CodigoGiftCard codigoGiftCard = service.cadastrar(
                request.getIdProduto(),
                request.getCodigo()
        );

        CodigoGiftCardResponse response = new CodigoGiftCardResponse(
                codigoGiftCard.getId(),
                codigoGiftCard.getProduto().getId(),
                codigoGiftCard.getCodigo(),
                codigoGiftCard.getStatus()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
