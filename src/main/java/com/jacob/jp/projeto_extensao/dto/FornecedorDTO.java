package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Fornecedor;
import com.jacob.jp.projeto_extensao.model.Produto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class FornecedorDTO {

    private Integer id;
    private String nome;
    private List<Integer> idsProdutos;

    public FornecedorDTO(Fornecedor fornecedor) {
        this.id = fornecedor.getId();
        this.nome = fornecedor.getNome();
        this.idsProdutos = fornecedor.getProdutos() != null
                ? fornecedor.getProdutos().stream().map(Produto::getId).toList()
                : List.of();
    }
}
