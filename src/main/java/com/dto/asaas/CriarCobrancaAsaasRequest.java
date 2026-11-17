package com.dto.asaas;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarCobrancaAsaasRequest(
        String customer,
        String billingType,
        BigDecimal value,
        LocalDate dueDate,
        String description,
        String externalReference
) {
}