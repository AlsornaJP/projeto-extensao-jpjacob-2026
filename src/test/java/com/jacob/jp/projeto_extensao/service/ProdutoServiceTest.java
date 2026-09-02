package com.jacob.jp.projeto_extensao.service;

import com.jacob.jp.projeto_extensao.dto.AtualizarProdutoDTO;
import com.jacob.jp.projeto_extensao.dto.FornecedorDTO;
import com.jacob.jp.projeto_extensao.dto.ProdutoDTO;
import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import com.jacob.jp.projeto_extensao.exception.RecursoNaoEncontradoException;
import com.jacob.jp.projeto_extensao.exception.RegraDeNegocioException;
import com.jacob.jp.projeto_extensao.model.Fornecedor;
import com.jacob.jp.projeto_extensao.model.Produto;
import com.jacob.jp.projeto_extensao.model.Variante;
import com.jacob.jp.projeto_extensao.repository.FornecedorRepository;
import com.jacob.jp.projeto_extensao.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private static Fornecedor fornecedorComId(Integer id) {
        FornecedorDTO dto = new FornecedorDTO();
        dto.setId(id);
        dto.setNome("Fazenda Minas");
        return new Fornecedor(dto);
    }

    private static VarianteDTO varianteDTO(String medida, String preco, Integer estoque) {
        VarianteDTO dto = new VarianteDTO();
        dto.setMedida(medida);
        dto.setPreco(new BigDecimal(preco));
        dto.setEstoque(estoque);
        return dto;
    }

    private static ProdutoDTO dtoValido() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Queijo canastra");
        dto.setDescricao("Meia cura");
        dto.setIdFornecedor(7);
        dto.setVariantes(new ArrayList<>(List.of(
                varianteDTO("500g", "49.90", 12),
                varianteDTO("1kg", "89.90", 5))));
        return dto;
    }

    private static AtualizarProdutoDTO atualizarDTO(String nome, Integer idFornecedor) {
        AtualizarProdutoDTO dto = new AtualizarProdutoDTO();
        dto.setNome(nome);
        dto.setDescricao("Meia cura");
        dto.setIdFornecedor(idFornecedor);
        return dto;
    }

    private static Produto produtoSalvo() {
        Produto produto = new Produto(dtoValido(), fornecedorComId(7));
        for (VarianteDTO varianteDTO : dtoValido().getVariantes()) {
            produto.adicionarVariante(new Variante(varianteDTO, produto));
        }
        return produto;
    }

    @Test
    void criarResolveOFornecedorEIgnoraIdVindoDoCliente() {
        ProdutoDTO dto = dtoValido();
        dto.setId(3); // cliente mandou id: nao pode virar update do produto 3
        dto.getVariantes().get(0).setId(99);
        when(fornecedorRepository.findById(7)).thenReturn(Optional.of(fornecedorComId(7)));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        ProdutoDTO salvo = produtoService.criar(dto);

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getVariantes()).allSatisfy(v -> assertThat(v.getId()).isNull());
        assertThat(captor.getValue().getFornecedor().getId()).isEqualTo(7);
        assertThat(salvo.getNome()).isEqualTo("Queijo canastra");
        assertThat(salvo.getIdFornecedor()).isEqualTo(7);
    }

    @Test
    void criarPersisteAsVariantesVinculadasAoProduto() {
        when(fornecedorRepository.findById(7)).thenReturn(Optional.of(fornecedorComId(7)));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        ProdutoDTO salvo = produtoService.criar(dtoValido());

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(captor.capture());
        Produto persistido = captor.getValue();
        assertThat(persistido.getVariantes()).hasSize(2);
        assertThat(persistido.getVariantes()).allSatisfy(v ->
                assertThat(v.getProduto()).isSameAs(persistido));
        assertThat(salvo.getVariantes()).extracting(VarianteDTO::getMedida)
                .containsExactly("500g", "1kg");
    }

    @Test
    void criarSemEstoqueInformadoNaVarianteUsaZero() {
        ProdutoDTO dto = dtoValido();
        dto.getVariantes().get(0).setEstoque(null);
        when(fornecedorRepository.findById(7)).thenReturn(Optional.of(fornecedorComId(7)));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        assertThat(produtoService.criar(dto).getVariantes().get(0).getEstoque()).isZero();
    }

    @Test
    void criarFalhaComMedidaRepetidaNoMesmoProduto() {
        ProdutoDTO dto = dtoValido();
        dto.getVariantes().get(1).setMedida("500g");

        assertThatThrownBy(() -> produtoService.criar(dto))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("500g");

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void criarFalhaQuandoFornecedorNaoExiste() {
        when(fornecedorRepository.findById(7)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.criar(dtoValido()))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Fornecedor 7 nao encontrado");

        verify(produtoRepository, never()).save(any());
    }

    @Test
    void listarDevolveUmDtoPorProdutoComSuasVariantes() {
        when(produtoRepository.findAll()).thenReturn(List.of(produtoSalvo()));

        List<ProdutoDTO> encontrados = produtoService.listar();

        assertThat(encontrados).hasSize(1);
        assertThat(encontrados.get(0).getVariantes()).hasSize(2);
    }

    @Test
    void buscarPorIdFalhaQuandoProdutoNaoExiste() {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.buscarPorId(99))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produto 99 nao encontrado");
    }

    @Test
    void atualizarAlteraCamposETrocaOFornecedor() {
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoSalvo()));
        when(fornecedorRepository.findById(8)).thenReturn(Optional.of(fornecedorComId(8)));

        ProdutoDTO atualizado = produtoService.atualizar(1, atualizarDTO("Queijo canastra curado", 8));

        assertThat(atualizado.getNome()).isEqualTo("Queijo canastra curado");
        assertThat(atualizado.getIdFornecedor()).isEqualTo(8);
    }

    @Test
    void atualizarProdutoNaoMexeNasVariantes() {
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoSalvo()));
        when(fornecedorRepository.findById(7)).thenReturn(Optional.of(fornecedorComId(7)));

        ProdutoDTO atualizado = produtoService.atualizar(1, atualizarDTO("Queijo canastra curado", 7));

        assertThat(atualizado.getVariantes()).extracting(VarianteDTO::getMedida)
                .containsExactly("500g", "1kg");
        assertThat(atualizado.getVariantes().get(0).getPreco()).isEqualByComparingTo("49.90");
    }

    @Test
    void atualizarFalhaQuandoProdutoNaoExiste() {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.atualizar(99, atualizarDTO("Queijo canastra", 7)))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void deletarRemoveOProdutoEncontrado() {
        Produto existente = produtoSalvo();
        when(produtoRepository.findById(1)).thenReturn(Optional.of(existente));

        produtoService.deletar(1);

        verify(produtoRepository).delete(existente);
    }

    @Test
    void deletarFalhaQuandoProdutoNaoExiste() {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.deletar(99))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(produtoRepository, never()).delete(any());
    }
}
