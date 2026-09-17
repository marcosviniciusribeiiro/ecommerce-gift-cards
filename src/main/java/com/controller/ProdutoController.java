package com.controller;

import com.dto.ProdutoRequest;
import com.dto.ProdutoResponse;
import com.model.Produto;
import com.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/new")
    public ResponseEntity<ProdutoResponse> cadastrar(@Valid @RequestBody ProdutoRequest request){
        Produto produto = produtoService.cadastrar(request);

        ProdutoResponse response = converterParaResponse(produto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProdutoResponse>> listarTodos(){
        List<ProdutoResponse> produtos = produtoService
                .listarTodos()
                .stream()
                .map(this::converterParaResponse)
                .toList();

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Integer id){
        Produto produto = produtoService.buscarPorId(id);

        return ResponseEntity.ok(converterParaResponse(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProdutoRequest request
    ){
        Produto produto = produtoService.atualizar(id, request);

        return ResponseEntity.ok(converterParaResponse(produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id){
        produtoService.deletar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    private ProdutoResponse converterParaResponse(Produto produto){
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPlataforma(),
                produto.getValor()
        );
    }
}