package com.jacob.jp.projeto_extensao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="carrinhos")
@NoArgsConstructor
@Getter
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="preco_total", nullable=false, precision=10, scale=2)
    private BigDecimal precoTotal;

    @OneToOne(mappedBy = "carrinho")
    private Cliente cliente;

    @OneToMany(mappedBy="carrinho", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Item> itens = new ArrayList<>();
}
