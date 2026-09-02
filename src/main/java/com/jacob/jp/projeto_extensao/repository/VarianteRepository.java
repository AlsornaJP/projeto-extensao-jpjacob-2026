package com.jacob.jp.projeto_extensao.repository;

import com.jacob.jp.projeto_extensao.model.Variante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarianteRepository extends JpaRepository<Variante, Integer> {
    Optional<Variante> findByProdutoIdAndMedida(Integer idProduto, String medida);

    List<Variante> findByProdutoIdOrderByPrecoAsc(Integer idProduto);

    long countByProdutoId(Integer idProduto);
}
