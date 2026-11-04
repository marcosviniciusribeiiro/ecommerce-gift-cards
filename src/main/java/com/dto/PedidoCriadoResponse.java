package com.dto;

import com.model.StatusPedido;
import java.math.BigDecimal;

public record PedidoCriadoResponse(
        Integer id,
        StatusPedido status,
        BigDecimal valorTotal
) {}