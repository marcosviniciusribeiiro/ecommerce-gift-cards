package com.dto;

public class EstoqueProdutoResponse {
    private Integer idProduto;

    private long quantidadeDisponivel;

    public EstoqueProdutoResponse(
            Integer idProduto,
            long quantidadeDisponivel
    ) {
        this.idProduto = idProduto;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }

    public Integer getIdProduto() {
        return idProduto;
    }

    public long getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }
}
