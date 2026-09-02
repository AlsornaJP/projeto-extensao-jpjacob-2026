package com.jacob.jp.projeto_extensao.service;

import com.jacob.jp.projeto_extensao.dto.ProdutoDTO;
import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import com.jacob.jp.projeto_extensao.exception.RecursoNaoEncontradoException;
import com.jacob.jp.projeto_extensao.exception.RegraDeNegocioException;
import com.jacob.jp.projeto_extensao.model.Fornecedor;
import com.jacob.jp.projeto_extensao.model.Produto;
import com.jacob.jp.projeto_extensao.model.Variante;
import com.jacob.jp.projeto_extensao.repository.FornecedorRepository;
import com.jacob.jp.projeto_extensao.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FornecedorRepository fornecedorRepository) {
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listar() {
        return produtoRepository.findAll().stream()
                .map(ProdutoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Integer id) {
        return new ProdutoDTO(buscarProduto(id));
    }

    @Transactional
    public ProdutoDTO criar(ProdutoDTO dto) {
        // O id e gerado pelo banco: um id vindo do cliente transformaria o insert em update.
        dto.setId(null);
        garantirMedidasDistintas(dto.getVariantes());
        Produto produto = new Produto(dto, buscarFornecedor(dto.getIdFornecedor()));
        for (VarianteDTO varianteDTO : dto.getVariantes()) {
            varianteDTO.setId(null);
            varianteDTO.setIdProduto(null);
            produto.adicionarVariante(new Variante(varianteDTO, produto));
        }
        return new ProdutoDTO(produtoRepository.save(produto));
    }

    @Transactional
    public ProdutoDTO atualizar(Integer id, ProdutoDTO dto) {
        Produto produto = buscarProduto(id);
        produto.atualizarDados(
                dto.getNome(),
                dto.getDescricao(),
                buscarFornecedor(dto.getIdFornecedor()));
        return new ProdutoDTO(produto);
    }

    @Transactional
    public void deletar(Integer id) {
        produtoRepository.delete(buscarProduto(id));
    }

    private Produto buscarProduto(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    private Fornecedor buscarFornecedor(Integer id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fornecedor", id));
    }

    private void garantirMedidasDistintas(List<VarianteDTO> variantes) {
        Set<String> vistas = new HashSet<>();
        for (VarianteDTO variante : variantes) {
            if (!vistas.add(variante.getMedida())) {
                throw new RegraDeNegocioException(
                        "Medida repetida no mesmo produto: " + variante.getMedida());
            }
        }
    }
}
