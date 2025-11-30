package org.sistema.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente; //Ex: "Mesa 11"
    private String status;  //Ex: "RESERVA_SOLICITADA", "ESTOQUE_CONFIRMADO"

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ItemPedido> itens;

    
    // Construtor obrigatório pelo JPA
    public Pedido() {
    }

    public Pedido(String cliente, String status) {
        this.cliente = cliente;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
        if (itens != null) {
            itens.forEach(item -> item.setPedido(this));
        }
    }
}