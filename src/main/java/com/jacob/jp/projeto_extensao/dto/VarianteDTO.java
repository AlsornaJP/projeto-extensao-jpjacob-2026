package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Variante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class VarianteDTO {
    private Integer id;

    @NotBlank
    @Size(max = 50)
    private String medida;

    @NotNull
    @Positive
    private BigDecimal preco;

    @PositiveOrZero
    private Integer estoque;

    private Integer idProduto;

    public VarianteDTO(Variante variante) {
        this.id = variante.getId();
        this.medida = variante.getMedida();
        this.preco = variante.getPreco();
        this.estoque = variante.getEstoque();
        this.idProduto = variante.getProduto() != null ? variante.getProduto().getId() : null;
    }
}
