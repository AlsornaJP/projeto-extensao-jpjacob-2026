package com.jacob.jp.projeto_extensao.service;

import com.jacob.jp.projeto_extensao.dto.FornecedorDTO;
import com.jacob.jp.projeto_extensao.dto.ProdutoDTO;
import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import com.jacob.jp.projeto_extensao.exception.RecursoNaoEncontradoException;
import com.jacob.jp.projeto_extensao.exception.RegraDeNegocioException;
import com.jacob.jp.projeto_extensao.model.Fornecedor;
import com.jacob.jp.projeto_extensao.model.Produto;
import com.jacob.jp.projeto_extensao.model.Variante;
import com.jacob.jp.projeto_extensao.repository.ProdutoRepository;
import com.jacob.jp.projeto_extensao.repository.VarianteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VarianteServiceTest {
    @Mock
    private VarianteRepository varianteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private VarianteService varianteService;

    private static Produto produtoComId(Integer id) {
        FornecedorDTO fornecedorDTO = new FornecedorDTO();
        fornecedorDTO.setId(7);
        fornecedorDTO.setNome("Fazenda Minas");

        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(id);
        dto.setNome("Queijo canastra");
        dto.setDescricao("Meia cura");
        return new Produto(dto, new Fornecedor(fornecedorDTO));
    }

    private static VarianteDTO varianteDTO(String medida, String preco, Integer estoque) {
        VarianteDTO dto = new VarianteDTO();
        dto.setMedida(medida);
        dto.setPreco(new BigDecimal(preco));
        dto.setEstoque(estoque);
        return dto;
    }

    private static Variante varianteExistente(Integer id, String medida, String preco, Integer estoque) {
        VarianteDTO dto = varianteDTO(medida, preco, estoque);
        dto.setId(id);
        return new Variante(dto, produtoComId(1));
    }

    @Test
    void adicionarVinculaAVarianteAoProduto() {
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoComId(1)));
        when(varianteRepository.findByProdutoIdAndMedida(1, "2kg")).thenReturn(Optional.empty());
        when(varianteRepository.save(any(Variante.class))).thenAnswer(i -> i.getArgument(0));

        VarianteDTO salva = varianteService.adicionar(1, varianteDTO("2kg", "159.90", 4));

        ArgumentCaptor<Variante> captor = ArgumentCaptor.forClass(Variante.class);
        verify(varianteRepository).save(captor.capture());
        assertThat(captor.getValue().getProduto().getId()).isEqualTo(1);
        assertThat(salva.getMedida()).isEqualTo("2kg");
        assertThat(salva.getEstoque()).isEqualTo(4);
    }

    @Test
    void adicionarSemEstoqueInformadoUsaZero() {
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoComId(1)));
        when(varianteRepository.findByProdutoIdAndMedida(1, "2kg")).thenReturn(Optional.empty());
        when(varianteRepository.save(any(Variante.class))).thenAnswer(i -> i.getArgument(0));

        VarianteDTO dto = varianteDTO("2kg", "159.90", null);

        assertThat(varianteService.adicionar(1, dto).getEstoque()).isZero();
    }

    @Test
    void adicionarFalhaQuandoProdutoNaoExiste() {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> varianteService.adicionar(99, varianteDTO("2kg", "159.90", 4)))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produto 99 nao encontrado");

        verify(varianteRepository, never()).save(any());
    }

    @Test
    void adicionarFalhaQuandoAMedidaJaExisteNoProduto() {
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoComId(1)));
        when(varianteRepository.findByProdutoIdAndMedida(1, "500g"))
                .thenReturn(Optional.of(varianteExistente(5, "500g", "49.90", 12)));

        assertThatThrownBy(() -> varianteService.adicionar(1, varianteDTO("500g", "51.90", 3)))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("500g");

        verify(varianteRepository, never()).save(any());
    }

    @Test
    void atualizarAlteraMedidaEPrecoSemMexerNoEstoque() {
        Variante existente = varianteExistente(5, "500g", "49.90", 12);
        when(varianteRepository.findById(5)).thenReturn(Optional.of(existente));
        when(varianteRepository.findByProdutoIdAndMedida(1, "600g")).thenReturn(Optional.empty());

        VarianteDTO novosDados = varianteDTO("600g", "54.90", 999);
        VarianteDTO atualizada = varianteService.atualizar(5, novosDados);

        assertThat(atualizada.getMedida()).isEqualTo("600g");
        assertThat(atualizada.getPreco()).isEqualByComparingTo("54.90");
        assertThat(atualizada.getEstoque()).isEqualTo(12);
    }

    @Test
    void atualizarMantendoAPropriaMedidaNaoFalha() {
        Variante existente = varianteExistente(5, "500g", "49.90", 12);
        when(varianteRepository.findById(5)).thenReturn(Optional.of(existente));
        when(varianteRepository.findByProdutoIdAndMedida(1, "500g")).thenReturn(Optional.of(existente));

        VarianteDTO novosDados = varianteDTO("500g", "54.90", 12);
        VarianteDTO atualizada = varianteService.atualizar(5, novosDados);

        assertThat(atualizada.getPreco()).isEqualByComparingTo("54.90");
        assertThat(atualizada.getMedida()).isEqualTo("500g");
    }

    @Test
    void baixarEstoqueSubtraiAQuantidade() {
        when(varianteRepository.findById(5))
                .thenReturn(Optional.of(varianteExistente(5, "500g", "49.90", 12)));

        assertThat(varianteService.baixarEstoque(5, 4).getEstoque()).isEqualTo(8);
    }

    @Test
    void baixarEstoqueFalhaQuandoNaoHaSaldoSuficiente() {
        when(varianteRepository.findById(5))
                .thenReturn(Optional.of(varianteExistente(5, "500g", "49.90", 3)));

        assertThatThrownBy(() -> varianteService.baixarEstoque(5, 4))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Estoque insuficiente");
    }

    @Test
    void reporEstoqueSomaAQuantidade() {
        when(varianteRepository.findById(5))
                .thenReturn(Optional.of(varianteExistente(5, "500g", "49.90", 3)));

        assertThat(varianteService.reporEstoque(5, 7).getEstoque()).isEqualTo(10);
    }

    @Test
    void listarPorProdutoDevolveAsVariantesOrdenadasPorPreco() {
        when(varianteRepository.findByProdutoIdOrderByPrecoAsc(1)).thenReturn(List.of(
                varianteExistente(5, "500g", "49.90", 12),
                varianteExistente(6, "1kg", "89.90", 5)));

        assertThat(varianteService.listarPorProduto(1))
                .extracting(VarianteDTO::getMedida)
                .containsExactly("500g", "1kg");
    }

    @Test
    void deletarRemoveAVarianteEncontrada() {
        Variante existente = varianteExistente(5, "500g", "49.90", 12);
        when(varianteRepository.findById(5)).thenReturn(Optional.of(existente));

        varianteService.deletar(5);

        verify(varianteRepository).delete(existente);
    }

    @Test
    void deletarFalhaQuandoAVarianteNaoExiste() {
        when(varianteRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> varianteService.deletar(99))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Variante 99 nao encontrada");

        verify(varianteRepository, never()).delete(any());
    }
}
