
package com.dto;

import java.math.BigDecimal;

public record PagamentoCriadoResponse(
        Integer idPagamento,
        Integer idPedido,
        String idPagamentoAsaas,
        BigDecimal valor,
        String status,
        String invoiceUrl
) {
}
