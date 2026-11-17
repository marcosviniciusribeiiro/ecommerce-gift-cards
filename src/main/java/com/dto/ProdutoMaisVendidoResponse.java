
package com.dto;

import java.math.BigDecimal;

public record ProdutoMaisVendidoResponse(
        Integer idProduto,
        String nomeProduto,
        Long quantidadeVendida,
        BigDecimal faturamento
) {
}