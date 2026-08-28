package com.jacob.jp.projeto_extensao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="produtos")
@NoArgsConstructor
@Getter
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="nome_produto", nullable=false, length=150)
    private String nome;

    @Column(name="descricao", nullable=false, length=500)
    private String descricao;

    @Column(name="preco", nullable=false, precision=10, scale=2)
    private BigDecimal preco;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_fornecedor", referencedColumnName="id")
    private Fornecedor fornecedor;
}
