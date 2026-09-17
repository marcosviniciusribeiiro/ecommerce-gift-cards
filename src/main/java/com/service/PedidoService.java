package com.service;

import com.dto.PedidoConfirmadoResponse;
import com.dto.PedidoResponse;
import com.exception.CodeConflictException;
import com.exception.CodeNotFoundException;
import com.exception.ProductNotFoundException;
import com.exception.UserNotFoundException;
import com.model.*;
import com.repository.ItemPedidoRepository;
import com.repository.PedidoRepository;
import com.repository.ProdutoRepository;
import com.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
            CodigoGiftCardService codigoGiftCardService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.codigoGiftCardService = codigoGiftCardService;
    }

    public Pedido criarPedido(
            String email,
            Integer idProduto
    ){
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado."));

        Produto produto = produtoRepository
                .findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.pendente);
        pedido.setValorTotal(produto.getValor());

        pedido = pedidoRepository.save(pedido);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setPedido(pedido);
        itemPedido.setProduto(produto);
        itemPedido.setValorUnitario(produto.getValor());
        itemPedidoRepository.save(itemPedido);

        return pedido;
    }

    public PedidoConfirmadoResponse confirmarPedido(Integer idPedido){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ProductNotFoundException("Pedido não encontrado."));

        if(pedido.getStatus() != StatusPedido.pendente){
            throw new CodeConflictException("Somente pedidos pendentes podem ser confirmados.");
        }

        ItemPedido itemPedido = itemPedidoRepository.findFirstByPedido(pedido)
                .orElseThrow(() ->
                        new ProductNotFoundException("Item do pedido não encontrado."));

        CodigoGiftCard codigoGiftCard = codigoGiftCardService
                .atribuirCodigo(
                        itemPedido.getProduto(),
                        itemPedido
                );

        pedido.setStatus(StatusPedido.pago);

        pedidoRepository.save(pedido);

        return new PedidoConfirmadoResponse(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                codigoGiftCard.getCodigo()
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
                .map(this::converterParaResponse)
                .toList();
    }

    private PedidoResponse converterParaResponse(Pedido pedido) {

        ItemPedido itemPedido = itemPedidoRepository.findFirstByPedido(pedido)
                .orElseThrow(() ->
                        new ProductNotFoundException("Item do produto não encontrado."));

        return new PedidoResponse(
                pedido.getId(),
                itemPedido.getProduto().getId(),
                pedido.getStatus(),
                pedido.getDataPedido(),
                pedido.getValorTotal()
        );
    }
}
