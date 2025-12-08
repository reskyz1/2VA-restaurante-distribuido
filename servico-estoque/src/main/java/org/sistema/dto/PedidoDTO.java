package org.sistema.dto;

import java.util.List;
import java.io.Serializable;

public class PedidoDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long id; 
    private String cliente;
    private String status;
    private List<ItemDTO> itens;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, String cliente, List<ItemDTO> itens) {
        this.id = id;
        this.cliente = cliente;
        this.itens = itens;
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

    public List<ItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemDTO> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", cliente='" + cliente + '\'' +
                ", status='" + status + '\'' +
                ", itens=" + itens +
                '}';
    }
}  