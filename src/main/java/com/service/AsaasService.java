package com.service;

import com.dto.asaas.CobrancaAsaasResponse;
import com.dto.asaas.CriarCobrancaAsaasRequest;
import com.dto.asaas.PixQrCodeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AsaasService {
    private final RestClient restClient;

    public AsaasService(
            @Value("${asaas.api.url}") String apiUrl,
            @Value("${asaas.api.key}") String apiKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("access_token", apiKey)
                .defaultHeader(
                        "User-Agent",
                        "GiftCardEcommerceAcademico/1.0"
                ).build();
    }

    public CobrancaAsaasResponse criarCobranca(CriarCobrancaAsaasRequest request) {
        CobrancaAsaasResponse resposta = restClient.post()
                .uri("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CobrancaAsaasResponse.class);

        if (resposta == null || resposta.id() == null) {
            throw new IllegalStateException("O Asaas não retornou um identificador de cobrança.");
        }

        return resposta;
    }

    public CobrancaAsaasResponse consultarCobranca(String idPagamentoAsaas) {
        CobrancaAsaasResponse resposta = restClient.get()
                .uri("/payments/{id}", idPagamentoAsaas)
                .retrieve()
                .body(CobrancaAsaasResponse.class);

        if (resposta == null || resposta.id() == null) {
            throw new IllegalStateException("Não foi possível consultar a cobrança no Asaas.");
        }

        return resposta;
    }

    public PixQrCodeResponse consultarQrCodePix(String idPagamentoAsaas) {
        PixQrCodeResponse resposta = restClient.get()
                .uri("/payments/{id}/pixQrCode", idPagamentoAsaas)
                .retrieve()
                .body(PixQrCodeResponse.class);

        if (resposta == null || !resposta.success()) {
            throw new IllegalStateException("Não foi possível obter o Qr Code Pix.");
        }

        return resposta;
    }
}

