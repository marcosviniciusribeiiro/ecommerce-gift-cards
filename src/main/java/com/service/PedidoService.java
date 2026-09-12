package com.service;

import com.model.*;
import com.repository.ItemPedidoRepository;
import com.repository.PedidoRepository;
import com.repository.ProdutoRepository;
import com.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PedidoService {
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoService(
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository,
            PedidoRepository pedidoRepository,
            ItemPedidoRepository itemPedidoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
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
}
