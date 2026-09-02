package com.jacob.jp.projeto_extensao.service;

import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import com.jacob.jp.projeto_extensao.exception.RecursoNaoEncontradoException;
import com.jacob.jp.projeto_extensao.exception.RegraDeNegocioException;
import com.jacob.jp.projeto_extensao.model.Produto;
import com.jacob.jp.projeto_extensao.model.Variante;
import com.jacob.jp.projeto_extensao.repository.ProdutoRepository;
import com.jacob.jp.projeto_extensao.repository.VarianteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VarianteService {
    private final VarianteRepository varianteRepository;
    private final ProdutoRepository produtoRepository;

    public VarianteService(VarianteRepository varianteRepository, ProdutoRepository produtoRepository) {
        this.varianteRepository = varianteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Transactional(readOnly = true)
    public List<VarianteDTO> listarPorProduto(Integer idProduto) {
        return varianteRepository.findByProdutoIdOrderByPrecoAsc(idProduto).stream()
                .map(VarianteDTO::new)
                .toList();
    }

    @Transactional
    public VarianteDTO adicionar(Integer idProduto, VarianteDTO dto) {
        Produto produto = buscarProduto(idProduto);
        dto.setId(null);
        garantirMedidaInedita(idProduto, dto.getMedida(), null);
        return new VarianteDTO(varianteRepository.save(new Variante(dto, produto)));
    }

    @Transactional
    public VarianteDTO atualizar(Integer id, VarianteDTO dto) {
        Variante variante = buscarVariante(id);
        garantirMedidaInedita(variante.getProduto().getId(), dto.getMedida(), id);
        variante.atualizarDados(dto.getMedida(), dto.getPreco());
        return new VarianteDTO(variante);
    }

    @Transactional
    public VarianteDTO baixarEstoque(Integer id, int quantidade) {
        Variante variante = buscarVariante(id);
        try {
            variante.baixarEstoque(quantidade);
        } catch (Variante.EstoqueInsuficiente ex) {
            throw new RegraDeNegocioException(ex.getMessage());
        }
        return new VarianteDTO(variante);
    }

    @Transactional
    public VarianteDTO reporEstoque(Integer id, int quantidade) {
        Variante variante = buscarVariante(id);
        variante.reporEstoque(quantidade);
        return new VarianteDTO(variante);
    }

    @Transactional
    public void deletar(Integer id) {
        varianteRepository.delete(buscarVariante(id));
    }

    private Produto buscarProduto(Integer id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    private Variante buscarVariante(Integer id) {
        return varianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Variante", id, "nao encontrada"));
    }

    private void garantirMedidaInedita(Integer idProduto, String medida, Integer idIgnorado) {
        Optional<Variante> existente = varianteRepository.findByProdutoIdAndMedida(idProduto, medida);
        if (existente.isEmpty()) {
            return;
        }
        boolean ehAPropria = idIgnorado != null && idIgnorado.equals(existente.get().getId());
        if (!ehAPropria) {
            throw new RegraDeNegocioException(
                    "O produto " + idProduto + " ja tem uma variante com a medida " + medida);
        }
    }
}
