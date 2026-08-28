package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ItemDTO {

    private Integer id;
    private Integer quantidade;
    private BigDecimal preco;
    private Integer idProduto;
    private Integer idCarrinho;

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.quantidade = item.getQuantidade();
        this.preco = item.getPreco();
        this.idProduto = item.getProduto() != null ? item.getProduto().getId() : null;
        this.idCarrinho = item.getCarrinho() != null ? item.getCarrinho().getId() : null;
    }
}
