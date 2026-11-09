package com.dto.asaas;

public record PixQrCodeResponse(
        boolean success,
        String encodedImage,
        String payload,
        String expirationDate,
        String description
) {
}