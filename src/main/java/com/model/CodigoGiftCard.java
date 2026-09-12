package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_codigos_giftcard")
public class CodigoGiftCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @OneToOne
    @JoinColumn(name = "id_item_pedido", unique = true)
    private ItemPedido itemPedido;

    @Column(unique = true, nullable = false)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCodigo status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public ItemPedido getItemPedido() {
        return itemPedido;
    }

    public void setItemPedido(ItemPedido itemPedido) {
        this.itemPedido = itemPedido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public StatusCodigo getStatus() {
        return status;
    }

    public void setStatus(StatusCodigo status) {
        this.status = status;
    }
}
