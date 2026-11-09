
package com.service;

import com.dto.PagamentoCriadoResponse;
import com.dto.asaas.CobrancaAsaasResponse;
import com.dto.asaas.CriarCobrancaAsaasRequest;
import com.dto.asaas.PixQrCodeResponse;
import com.model.FormaPagamento;
import com.model.Pagamento;
import com.model.Pedido;
import com.model.StatusPagamento;
import com.model.StatusPedido;
import com.repository.PagamentoRepository;
import com.repository.PedidoRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final AsaasService asaasService;
    private final String idClienteTeste;

    public PagamentoService(
            PagamentoRepository pagamentoRepository,
            PedidoRepository pedidoRepository,
            AsaasService asaasService,
            @Value("${asaas.sandbox.customer-id}") String idClienteTeste
    ) {
        this.pagamentoRepository = pagamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.asaasService = asaasService;
        this.idClienteTeste = idClienteTeste;
    }

    public PagamentoCriadoResponse criarPagamentoPix(
            Integer idPedido,
            String emailUsuario
    ) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Pedido não encontrado."
                        )
                );

        // O usuário só pode pagar pelos próprios pedidos.
        if (!pedido.getUsuario().getEmail().equals(emailUsuario)) {
            throw new SecurityException(
                    "Você não possui acesso a este pedido."
            );
        }

        if (pedido.getStatus() != StatusPedido.Pendente) {
            throw new IllegalStateException(
                    "Este pedido não está pendente."
            );
        }

        // Reutiliza uma cobrança pendente já registrada.
        List<Pagamento> pagamentos =
                pagamentoRepository.findByPedidoId(idPedido);

        for (Pagamento pagamento : pagamentos) {
            if (pagamento.getIdPagamentoAsaas() != null
                    && pagamento.getFormaPagamento() == FormaPagamento.PIX) {

                CobrancaAsaasResponse existente =
                        asaasService.consultarCobranca(
                                pagamento.getIdPagamentoAsaas()
                        );

                if ("PENDING".equals(existente.status())) {
                    return converterResposta(pagamento, existente);
                }

                if ("RECEIVED".equals(existente.status())
                        || "CONFIRMED".equals(existente.status())) {
                    throw new IllegalStateException("Já existe uma cobrança paga ou confirmada.");
                }

                throw new IllegalStateException("A cobrança existente precisa ser analisada: " + existente.status());
            }
        }

        CriarCobrancaAsaasRequest request =
                new CriarCobrancaAsaasRequest(
                        idClienteTeste,
                        "PIX",
                        pedido.getValorTotal(),
                        LocalDate.now().plusDays(3),
                        "Pagamento do pedido #" + pedido.getId(),
                        "PEDIDO-" + pedido.getId()
                );

        CobrancaAsaasResponse cobranca = asaasService.criarCobranca(request);

        Pagamento pagamento = new Pagamento(
                pedido,
                pedido.getValorTotal(),
                FormaPagamento.PIX
        );

        pagamento.setIdPagamentoAsaas(cobranca.id());

        pagamento.setStatus(
                StatusPagamento.valueOf(cobranca.status())
        );

        pagamento = pagamentoRepository.save(pagamento);

        return converterResposta(pagamento, cobranca);
    }

    public PixQrCodeResponse consultarQrCodePix(
            Integer idPagamento,
            String emailUsuario
    ) {
        Pagamento pagamento = pagamentoRepository.findById(idPagamento)
                .orElseThrow(
                        () -> new IllegalArgumentException("Pagamento não encontrado.")
                );

        Pedido pedido = pagamento.getPedido();

        if (!pedido.getUsuario().getEmail().equals(emailUsuario)) {
            throw new SecurityException("Você não possui acesso a este pagamento.");
        }

        if (pagamento.getIdPagamentoAsaas() == null) {
            throw new IllegalStateException("Este pagamento ainda não possui uma cobrança no Asaas.");
        }

        return asaasService.consultarQrCodePix(
                pagamento.getIdPagamentoAsaas()
        );
    }

    private PagamentoCriadoResponse converterResposta(
            Pagamento pagamento,
            CobrancaAsaasResponse cobranca
    ) {
        return new PagamentoCriadoResponse(
                pagamento.getId(),
                pagamento.getPedido().getId(),
                cobranca.id(),
                pagamento.getValor(),
                cobranca.status(),
                cobranca.invoiceUrl()
        );
    }
}