package org.sistema.model;

import jakarta.persistence.*;

@Entity
@Table(name = "item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long produtoId;
    private int quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id") // Coluna na tabela item_pedido que referencia o pedido
    private Pedido pedido;
    
    // Construtor obrigatório pelo JPA
    public ItemPedido() {
    }

    public ItemPedido(Long produtoId, int quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}