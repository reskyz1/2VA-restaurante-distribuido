package org.sistema.dto;

import java.io.Serializable;

public class ItemDTO implements java.io.Serializable   {

    private int produtoId;
    private int quantidade;

    public ItemDTO() {
    }

    public ItemDTO(int produtoId, int quantidade) {
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
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
