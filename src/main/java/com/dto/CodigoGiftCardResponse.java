package com.dto;

import com.model.StatusCodigo;

public class CodigoGiftCardResponse {
    private Integer id;

    private Integer idProduto;

    private String codigo;

    private StatusCodigo status;

    public CodigoGiftCardResponse(
            Integer id,
            Integer idProduto,
            String codigo,
            StatusCodigo status
    ) {
        this.id = id;
        this.idProduto = idProduto;
        this.codigo = codigo;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public String getCodigo() {
        return codigo;
    }

    public StatusCodigo getStatus() {
        return status;
    }
}
