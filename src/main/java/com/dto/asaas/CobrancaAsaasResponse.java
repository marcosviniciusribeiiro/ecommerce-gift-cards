package com.dto.asaas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CobrancaAsaasResponse(
        String id,
        String customer,
        String billingType,
        BigDecimal value,
        String status,
        String invoiceUrl,
        String externalReference
) {
}