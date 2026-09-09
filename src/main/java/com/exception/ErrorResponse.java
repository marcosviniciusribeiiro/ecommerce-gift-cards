package com.exception;

public class ErrorResponse {
    private int status;
    private String mensagem;

    public ErrorResponse(
            int status,
            String mensagem
    ) {
        this.status = status;
        this.mensagem = mensagem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
