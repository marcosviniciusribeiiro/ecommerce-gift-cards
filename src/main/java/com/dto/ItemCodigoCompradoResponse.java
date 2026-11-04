package com.dto;

public class ItemCodigoCompradoResponse {
    private Integer idItemPedido;
    private Integer idProduto;
    private String nomeProduto;
    private String codigo;

    public ItemCodigoCompradoResponse(
            Integer idItemPedido,
            Integer idProduto,
            String nomeProduto,
            String codigo
    ) {
        this.idItemPedido = idItemPedido;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.codigo = codigo;
    }

    public Integer getIdItemPedido() {
        return idItemPedido;
    }

    public void setIdItemPedido(Integer idItemPedido) {
        this.idItemPedido = idItemPedido;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Integer idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
