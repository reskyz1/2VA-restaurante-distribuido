package org.sistema.dto;

import java.util.List;
import java.io.Serializable;

public class PedidoDTO implements java.io.Serializable   {

    private String cliente;
    private List<ItemDTO> itens;

    public PedidoDTO() {
    }

    public PedidoDTO(String cliente, List<ItemDTO> itens) {
        this.cliente = cliente;
        this.itens = itens;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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
                "cliente='" + cliente + '\'' +
                ", itens=" + itens +
                '}';
    }
}
