package com.jacob.jp.projeto_extensao.service;

import com.jacob.jp.projeto_extensao.dto.FornecedorDTO;
import com.jacob.jp.projeto_extensao.dto.ProdutoDTO;
import com.jacob.jp.projeto_extensao.exception.RecursoNaoEncontradoException;
import com.jacob.jp.projeto_extensao.model.Fornecedor;
import com.jacob.jp.projeto_extensao.model.Produto;
import com.jacob.jp.projeto_extensao.repository.FornecedorRepository;
import com.jacob.jp.projeto_extensao.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    private static ProdutoDTO dtoValido() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Queijo canastra");
        dto.setDescricao("Meia cura, 1kg");
        dto.setPreco(new BigDecimal("89.90"));
        dto.setEstoque(12);
        dto.setIdFornecedor(7);
        return dto;
    }

    @Test
    void criarResolveOFornecedorEIgnoraIdVindoDoCliente() {
        ProdutoDTO dto = dtoValido();
        dto.setId(3); // cliente mandou id: nao pode virar update do produto 3
        when(fornecedorRepository.findById(7)).thenReturn(Optional.of(fornecedorComId(7)));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        ProdutoDTO salvo = produtoService.criar(dto);

        ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isNull();
        assertThat(captor.getValue().getFornecedor().getId()).isEqualTo(7);
        assertThat(salvo.getNome()).isEqualTo("Queijo canastra");
        assertThat(salvo.getEstoque()).isEqualTo(12);
        assertThat(salvo.getIdFornecedor()).isEqualTo(7);
    }

    @Test
    void criarSemEstoqueInformadoUsaZero() {
        ProdutoDTO dto = dtoValido();
        dto.setEstoque(null);
        when(fornecedorRepository.findById(7)).thenReturn(Optional.of(fornecedorComId(7)));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        assertThat(produtoService.criar(dto).getEstoque()).isZero();
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
    void buscarPorIdFalhaQuandoProdutoNaoExiste() {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.buscarPorId(99))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produto 99 nao encontrado");
    }

    @Test
    void atualizarAlteraCamposETrocaOFornecedor() {
        Produto existente = new Produto(dtoValido(), fornecedorComId(7));
        when(produtoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fornecedorRepository.findById(8)).thenReturn(Optional.of(fornecedorComId(8)));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        ProdutoDTO novosDados = dtoValido();
        novosDados.setNome("Queijo canastra curado");
        novosDados.setPreco(new BigDecimal("109.90"));
        novosDados.setIdFornecedor(8);

        ProdutoDTO atualizado = produtoService.atualizar(1, novosDados);

        assertThat(atualizado.getNome()).isEqualTo("Queijo canastra curado");
        assertThat(atualizado.getPreco()).isEqualByComparingTo("109.90");
        assertThat(atualizado.getIdFornecedor()).isEqualTo(8);
    }

    @Test
    void atualizarNaoAlteraOEstoque() {
        Produto existente = new Produto(dtoValido(), fornecedorComId(7)); // estoque 12
        when(produtoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(fornecedorRepository.findById(7)).thenReturn(Optional.of(fornecedorComId(7)));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        ProdutoDTO novosDados = dtoValido();
        novosDados.setEstoque(999); // ignorado: estoque so muda por operacao dedicada

        assertThat(produtoService.atualizar(1, novosDados).getEstoque()).isEqualTo(12);
    }

    @Test
    void deletarFalhaQuandoProdutoNaoExiste() {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.deletar(99))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(produtoRepository, never()).delete(any());
    }
}
