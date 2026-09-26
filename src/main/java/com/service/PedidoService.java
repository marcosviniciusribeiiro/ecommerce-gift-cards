package com.service;

import com.dto.CodigoCompradoResponse;
import com.dto.PedidoAdmResponse;
import com.dto.PedidoConfirmadoResponse;
import com.dto.PedidoResponse;
import com.exception.*;
import com.model.*;
import com.repository.*;
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
                .orElseThrow(() -> new RuntimeException("E-mail não encontrado."));

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

    public PedidoConfirmadoResponse confirmarPedido(Integer idPedido){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() ->
                        new ProductNotFoundException("Pedido não encontrado."));

        if(pedido.getStatus() != StatusPedido.Pendente){
            throw new CodeConflictException("Somente pedidos pendentes podem ser confirmados.");
        }

        ItemPedido itemPedido = itemPedidoRepository.findFirstByPedido(pedido)
                .orElseThrow(() ->
                        new PedidoNotFoundException("Item do pedido não encontrado."));

        CodigoGiftCard codigoGiftCard = codigoGiftCardService
                .atribuirCodigo(
                        itemPedido.getProduto(),
                        itemPedido
                );

        pedido.setStatus(StatusPedido.Pago);

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

    public PedidoResponse buscarPorId(
            Integer id,
            String email){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado."));

        Pedido pedido = pedidoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado."));

        return converterParaResponse(pedido);
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

        return converterParaResponse(pedido);
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

        ItemPedido itemPedido = itemPedidoRepository.findFirstByPedido(pedido)
                .orElseThrow(() ->
                        new ItemPedidoNotFoundException("Item do pedido não encontrado."));

        CodigoGiftCard codigo = codigoGiftCardService.buscarPorItemPedido(itemPedido);

        return new CodigoCompradoResponse(
                pedido.getId(),
                itemPedido.getProduto().getId(),
                itemPedido.getProduto().getNome(),
                codigo.getCodigo()
        );
    }

    public List<PedidoAdmResponse> todosPedidos(){
        return pedidoRepository
                .findAll()
                .stream()
                .map(this::converterParaAdmResponse)
                .toList();
    }

    private PedidoResponse converterParaResponse(Pedido pedido) {

        ItemPedido itemPedido = itemPedidoRepository.findFirstByPedido(pedido)
                .orElseThrow(() ->
                        new ItemPedidoNotFoundException("Item pedido não encontrado."));

        return new PedidoResponse(
                pedido.getId(),
                itemPedido.getProduto().getId(),
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
                pedido.getUsuario().getId(),
                usuario.getNome(),
                usuario.getEmail(),
                item.getProduto().getId(),
                pedido.getStatus(),
                pedido.getDataPedido(),
                pedido.getValorTotal()
        );
    }
}