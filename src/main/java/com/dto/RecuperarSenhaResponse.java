package com.dto;

import java.time.LocalDateTime;

public class RecuperarSenhaResponse {
    private String token;
    private LocalDateTime dataExpiracao;

    public RecuperarSenhaResponse(
            String token,
            LocalDateTime dataExpiracao
    ) {
        this.token = token;
        this.dataExpiracao = dataExpiracao;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }
}
