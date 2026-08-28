package com.jacob.jp.projeto_extensao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="itens")
@NoArgsConstructor
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="quantidade", nullable=false)
    private Integer quantidade;

    @Column(name="preco", nullable=false, precision=10, scale=2)
    private BigDecimal preco;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_produto", referencedColumnName="id")
    private Produto produto;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_carrinho", referencedColumnName="id")
    private Carrinho carrinho;
}
