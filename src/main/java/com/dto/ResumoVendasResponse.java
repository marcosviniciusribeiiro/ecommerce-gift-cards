package com.dto;

import java.math.BigDecimal;

public record ResumoVendasResponse(
        Long totalPedidosPagos,
        Long totalGiftCardsVendidos,
        BigDecimal faturamentoTotal
) {
}