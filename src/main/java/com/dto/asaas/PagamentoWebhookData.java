
package com.dto.asaas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PagamentoWebhookData(
        String id,
        String customer,
        BigDecimal value,
        String billingType,
        String status,
        String externalReference
) {
}
