package com.jacob.jp.projeto_extensao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class AtualizarVarianteDTO {
    @NotBlank
    @Size(max = 50)
    private String medida;

    @NotNull
    @Positive
    private BigDecimal preco;
}
