package com.jacob.jp.projeto_extensao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReporEstoqueDTO {
    @NotNull
    @Positive
    private Integer quantidade;
}
