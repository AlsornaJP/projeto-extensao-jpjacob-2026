package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Produto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ProdutoDTO {

    private Integer id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer idFornecedor;

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.idFornecedor = produto.getFornecedor() != null ? produto.getFornecedor().getId() : null;
    }
}
