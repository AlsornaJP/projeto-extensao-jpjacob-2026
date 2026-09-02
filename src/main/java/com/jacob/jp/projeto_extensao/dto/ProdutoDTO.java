package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Produto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ProdutoDTO {
    private Integer id;

    @NotBlank
    @Size(max = 150)
    private String nome;

    @NotBlank
    @Size(max = 500)
    private String descricao;

    @NotNull
    private Integer idFornecedor;

    @NotEmpty
    @Valid
    private List<VarianteDTO> variantes = new ArrayList<>();

    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.idFornecedor = produto.getFornecedor() != null ? produto.getFornecedor().getId() : null;
        this.variantes = produto.getVariantes() != null
                ? produto.getVariantes().stream().map(VarianteDTO::new).toList()
                : List.of();
    }
}
