package com.jacob.jp.projeto_extensao.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProdutoDTOValidacaoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void abrirValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void fecharValidator() {
        factory.close();
    }

    private static VarianteDTO varianteValida() {
        VarianteDTO dto = new VarianteDTO();
        dto.setMedida("500g");
        dto.setPreco(new BigDecimal("49.90"));
        dto.setEstoque(12);
        return dto;
    }

    private static ProdutoDTO produtoValido() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Queijo canastra");
        dto.setDescricao("Meia cura");
        dto.setIdFornecedor(7);
        dto.setVariantes(new ArrayList<>(List.of(varianteValida())));
        return dto;
    }

    @Test
    void produtoCompletoNaoTemViolacoes() {
        assertThat(validator.validate(produtoValido())).isEmpty();
    }

    @Test
    void produtoSemVariantesEInvalido() {
        ProdutoDTO dto = produtoValido();
        dto.setVariantes(List.of());

        assertThat(validator.validate(dto))
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("variantes");
    }

    @Test
    void produtoSemNomeEInvalido() {
        ProdutoDTO dto = produtoValido();
        dto.setNome("   ");

        assertThat(validator.validate(dto))
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("nome");
    }

    @Test
    void produtoSemFornecedorEInvalido() {
        ProdutoDTO dto = produtoValido();
        dto.setIdFornecedor(null);

        assertThat(validator.validate(dto))
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("idFornecedor");
    }

    @Test
    void varianteAninhadaComPrecoZeradoInvalidaOProduto() {
        ProdutoDTO dto = produtoValido();
        dto.getVariantes().get(0).setPreco(BigDecimal.ZERO);

        assertThat(validator.validate(dto))
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("variantes[0].preco");
    }

    @Test
    void varianteAninhadaComEstoqueNegativoInvalidaOProduto() {
        ProdutoDTO dto = produtoValido();
        dto.getVariantes().get(0).setEstoque(-1);

        assertThat(validator.validate(dto))
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactly("variantes[0].estoque");
    }
}
