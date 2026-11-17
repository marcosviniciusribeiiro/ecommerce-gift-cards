package com.controller;

import com.dto.ProdutoMaisVendidoResponse;
import com.dto.ResumoVendasResponse;
import com.service.RelatorioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/relatorios")
public class RelatorioController {
    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoVendasResponse> obterResumoVendas(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim
    ) {

        ResumoVendasResponse resumo = relatorioService.obterResumoVendasPorPeriodo(inicio, fim);

        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<List<ProdutoMaisVendidoResponse>> obterProdutosMaisVendidos(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim
    ) {

        List<ProdutoMaisVendidoResponse> produtos = relatorioService.obterProdutosMaisVendidosPorPeriodo(inicio, fim);

        return ResponseEntity.ok(produtos);
    }
}
