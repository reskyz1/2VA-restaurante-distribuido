package org.sistema.dto;

import java.io.Serializable;

public class ItemDTO implements java.io.Serializable   {

    private static final long serialVersionUID = 1L;
    
    private Long produtoId;
    private int quantidade;

    public ItemDTO() {
    }

    public ItemDTO(Long produtoId, int quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
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

    @Override
    public String toString() {
        return "ItemDTO{" +
                "produtoId=" + produtoId +
                ", quantidade=" + quantidade +
                '}';
    }
}
