package com.dto;

public class CodigoCompradoResponse {
    private Integer idPedido;
    private Integer idProduto;
    private String nomeProduto;
    private String codigo;

    public CodigoCompradoResponse(
            Integer idPedido,
            Integer idProduto,
            String nomeProduto,
            String codigo
    ) {
        this.idPedido = idPedido;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.codigo = codigo;
    }

    public Integer getIdPedido() {
        return idPedido;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public String getCodigo() {
        return codigo;
    }
}
