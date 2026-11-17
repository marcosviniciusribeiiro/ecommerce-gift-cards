package com.service;

import com.dto.ProdutoMaisVendidoResponse;
import com.dto.ResumoVendasResponse;
import com.exception.PeriodoInvalidoException;
import com.repository.ItemPedidoRepository;
import com.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioService {
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public RelatorioService(
            PedidoRepository pedidoRepository,
            ItemPedidoRepository itemPedidoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Transactional(readOnly = true)
    public ResumoVendasResponse obterResumoVendas() {
        Long totalPedidosPagos = pedidoRepository.contarPedidosPagos();

        Long totalGiftCardsVendidos = itemPedidoRepository.contarGiftCardsVendidos();

        BigDecimal faturamentoTotal = pedidoRepository.calcularFaturamentoTotal();

        return new ResumoVendasResponse(
                totalPedidosPagos,
                totalGiftCardsVendidos,
                faturamentoTotal
        );
    }

    @Transactional(readOnly = true)
    public List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidos() {
        return itemPedidoRepository.buscarProdutosMaisVendidos();
    }

    @Transactional(readOnly = true)
    public ResumoVendasResponse obterResumoVendasPorPeriodo(
            LocalDate inicio,
            LocalDate fim
    ) {
        validarPeriodo(inicio, fim);

        LocalDateTime dataInicio = converterInicio(inicio);
        LocalDateTime dataFim = converterFim(fim);

        Long totalPedidosPagos = pedidoRepository.contarPedidosPagosPorPeriodo(dataInicio, dataFim);

        Long totalGiftCardsVendidos = itemPedidoRepository.contarGiftCardsVendidosPorPeriodo(dataInicio, dataFim);

        BigDecimal faturamentoTotal = pedidoRepository.calcularFaturamentoPorPeriodo(dataInicio, dataFim);

        return new ResumoVendasResponse(
                totalPedidosPagos,
                totalGiftCardsVendidos,
                faturamentoTotal
        );
    }

    @Transactional(readOnly = true)
    public List<ProdutoMaisVendidoResponse> obterProdutosMaisVendidosPorPeriodo(
            LocalDate inicio,
            LocalDate fim
    ) {
        validarPeriodo(inicio, fim);

        LocalDateTime dataInicio = converterInicio(inicio);
        LocalDateTime dataFim = converterFim(fim);

        return itemPedidoRepository.buscarProdutosMaisVendidosPorPeriodo(dataInicio, dataFim);
    }

    private LocalDateTime converterInicio(LocalDate inicio) {
        return inicio == null ? null : inicio.atStartOfDay();
    }

    private LocalDateTime converterFim(LocalDate fim) {
        return fim == null ? null : fim.plusDays(1).atStartOfDay();
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio != null && fim != null && inicio.isAfter(fim)) {
            throw new PeriodoInvalidoException(
                    "A data inicial não pode ser posterior à data final."
            );
        }
    }
}