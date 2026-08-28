package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Carrinho;
import com.jacob.jp.projeto_extensao.model.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CarrinhoDTO {

    private Integer id;
    private BigDecimal precoTotal;
    private Integer idCliente;
    private List<Integer> idsItens;

    public CarrinhoDTO(Carrinho carrinho) {
        this.id = carrinho.getId();
        this.precoTotal = carrinho.getPrecoTotal();
        this.idCliente = carrinho.getCliente() != null ? carrinho.getCliente().getId() : null;
        this.idsItens = carrinho.getItens() != null
                ? carrinho.getItens().stream().map(Item::getId).toList()
                : List.of();
    }
}
