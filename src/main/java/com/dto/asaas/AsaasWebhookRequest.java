
package com.dto.asaas;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AsaasWebhookRequest(
        String id,
        String event,
        String dateCreated,
        PagamentoWebhookData payment
) {
}
