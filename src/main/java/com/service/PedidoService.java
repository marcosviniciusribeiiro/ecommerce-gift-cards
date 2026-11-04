package com.service;

import com.dto.*;
import com.exception.*;
import com.model.*;
import com.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PedidoService {
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    private final CodigoGiftCardService codigoGiftCardService;

    public PedidoService(
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository,
            PedidoRepository pedidoRepository,
            ItemPedidoRepository itemPedidoRepository,
            CodigoGiftCardService codigoGiftCardService) {
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.codigoGiftCardService = codigoGiftCardService;
    }

    public Pedido criarPedido(
            String email,
            Integer idProduto){
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não encontrado."));

        Produto produto = produtoRepository
                .findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        long quantidadeDisponivel = codigoGiftCardService.contarDisponiveisPorProduto(idProduto);

        if (quantidadeDisponivel == 0){
            throw new EstoqueIndisponivelException(
                    "Não há códigos disponíveis para este produto."
            );
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.Pendente);
        pedido.setValorTotal(produto.getValor());

        pedido = pedidoRepository.save(pedido);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setValorUnitario(produto.getValor());
        itemPedidoRepository.save(itemPedido);

        return pedido;
    }

    @Transactional
    public Pedido criarPedidoCarrinho
            (String email,
             CriarPedidoCarrinhoRequest request) {
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("Usuário não encontrado.")
                );

        if (request == null ||
                request.getItens() == null ||
                request.getItens().isEmpty()) {
            throw new PedidoCarrinhoException("O carrinho não pode estar vazio.");
        }

        Map<Integer, Integer> quantidades = new HashMap<>();

        // Somar a qtd de produtos repetidos

        for (ItemCarrinhoRequest item : request.getItens()) {
            if (item == null ||
                    item.getIdProduto() == null ||
                    item.getQuantidade() == null ||
                    item.getQuantidade() <= 0) {
                throw new PedidoCarrinhoException("Produto ou quantidade inválida.");
            }

            quantidades.merge(
                    item.getIdProduto(),
                    item.getQuantidade(),
                    Math::addExact
            );
        }

        Map<Produto, Integer> produtos = new HashMap<>();

        BigDecimal valorTotal = BigDecimal.ZERO;

        // Validar produtos selecionados
        for (Map.Entry<Integer, Integer> entrada : quantidades.entrySet()) {
            Integer idProduto = entrada.getKey();
            Integer quantidade = entrada.getValue();

            Produto produto = produtoRepository
                    .findById(idProduto)
                    .orElseThrow(
                            () -> new ProductNotFoundException("Produto não encontrado.")
                    );

            long estoque = codigoGiftCardService
                    .contarDisponiveisPorProduto(idProduto);

            if (quantidade > estoque) {
                throw new EstoqueIndisponivelException(
                        "Estoque insuficiente para o produto: " + produto.getNome()
                );
            }

            produtos.put(produto, quantidade);

            BigDecimal subtotal = produto.getValor()
                    .multiply(BigDecimal.valueOf(quantidade));

            valorTotal = valorTotal.add(subtotal);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatus(StatusPedido.Pendente);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(valorTotal);

        pedido = pedidoRepository.save(pedido);

        // Criar um itemPedido para cada unidade do pedido
        for (Map.Entry<Produto, Integer> entrada : produtos.entrySet()) {
            Produto produto = entrada.getKey();
            Integer quantidade = entrada.getValue();

            for (int i = 0; i < quantidade; i++) {
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setPedido(pedido);
                itemPedido.setProduto(produto);
                itemPedido.setValorUnitario(produto.getValor());

                itemPedidoRepository.save(itemPedido);
            }
        }

        return pedido;
    }

    @Transactional
    public PedidoConfirmadoResponse confirmarPedido(Integer idPedido){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ProductNotFoundException("Pedido não encontrado."));

        if(pedido.getStatus() != StatusPedido.Pendente){
            throw new CodeConflictException("Somente pedidos pendentes podem ser confirmados.");
        }

        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);

        if (itens.isEmpty()) {
            throw new ItemPedidoNotFoundException("Nenhum item encontrado para este pedido.");
        }

        List<Integer> codigos = new ArrayList<>();

        for (ItemPedido item : itens) {
            CodigoGiftCard codigoGiftCard = codigoGiftCardService
                    .atribuirCodigo(
                            item.getProduto(),
                            item
                    );
            codigos.add(codigoGiftCard.getId());
        }

        pedido.setStatus(StatusPedido.Pago);

        pedidoRepository.save(pedido);

        return new PedidoConfirmadoResponse(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                codigos
        );
    }

    public List<PedidoResponse> pedidosUsuario(String email){
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado."));

        return pedidoRepository
                .findByUsuario(usuario)
                .stream()
                .map(this::converterParaPedidoResponse)
                .toList();
    }

    public PedidoResponse buscarPorId(
            Integer id,
            String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado."));

        Pedido pedido = pedidoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado."));

        return converterParaPedidoResponse(pedido);
    }

    public PedidoResponse cancelarPorId(
            Integer id,
            String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado."));

        Pedido pedido = pedidoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado."));

        if (pedido.getStatus() != StatusPedido.Pendente){
            throw new PedidoCanceladoException("Não foi possível cancelar o pedido.");
        }

        pedido.setStatus(StatusPedido.Cancelado);
        pedidoRepository.save(pedido);

        return converterParaPedidoResponse(pedido);
    }

    public CodigoCompradoResponse buscarCodigoComprado(
            Integer id,
            String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado."));

        Pedido pedido = pedidoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() ->
                        new PedidoNotFoundException("Pedido não encontrado."));

        if (pedido.getStatus() != StatusPedido.Pago) {
            throw new CodeConflictException(
                    "O código só está disponível para pedidos pagos."
            );
        }

        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);

        if (itens.isEmpty()) {
            throw new ItemPedidoNotFoundException("Nenhum item encontrado para este pedido.");
        }
        List<ItemCodigoCompradoResponse> codigos = new ArrayList<>();

        for (ItemPedido item : itens) {
            CodigoGiftCard codigo = codigoGiftCardService.buscarPorItemPedido(item);

            codigos.add(
                    new ItemCodigoCompradoResponse(
                            item.getId(),
                            item.getProduto().getId(),
                            item.getProduto().getNome(),
                            codigo.getCodigo()
                    )
            );
        }
        return new CodigoCompradoResponse(
                pedido.getId(),
                codigos
        );
    }

    public List<PedidoAdmResponse> todosPedidos(){
        return pedidoRepository
                .findAll()
                .stream()
                .map(this::converterParaAdmResponse)
                .toList();
    }

    private List<ItemPedidoResponse> converterItens(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);

        if (itens.isEmpty()) {
            throw new ItemPedidoNotFoundException("Nenhum item encontrado para este pedido.");
        }

        return itens.stream()
                .map(item -> new ItemPedidoResponse(
                        item.getId(),
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getValorUnitario()
                )).toList();
    }

    public PedidoResponse converterParaPedidoResponse(Pedido pedido) {

        ItemPedido itemPedido = itemPedidoRepository.findFirstByPedido(pedido)
                .orElseThrow(() ->
                        new ItemPedidoNotFoundException("Item pedido não encontrado."));

        return new PedidoResponse(
                pedido.getId(),
                converterItens(pedido),
                pedido.getStatus(),
                pedido.getDataPedido(),
                pedido.getValorTotal()
        );
    }

    private PedidoAdmResponse converterParaAdmResponse(Pedido pedido){
        Usuario usuario = pedido.getUsuario();

        ItemPedido item = itemPedidoRepository.findFirstByPedido(pedido)
                .orElseThrow(() ->
                        new ItemPedidoNotFoundException("Item pedido não encontrado."));

        return new PedidoAdmResponse(
                pedido.getId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                converterItens(pedido),
                pedido.getStatus(),
                pedido.getDataPedido(),
                pedido.getValorTotal()
        );
    }
}