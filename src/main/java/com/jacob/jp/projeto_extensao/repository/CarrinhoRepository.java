package com.jacob.jp.projeto_extensao.repository;

import com.jacob.jp.projeto_extensao.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Integer> {
}
