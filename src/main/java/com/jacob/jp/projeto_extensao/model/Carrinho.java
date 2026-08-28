package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.CarrinhoDTO;
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
    private Integer id;

    @Column(name="preco_total", nullable=false, precision=10, scale=2)
    private BigDecimal precoTotal;

    @OneToOne(mappedBy = "carrinho")
    private Cliente cliente;

    @OneToMany(mappedBy="carrinho", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Item> itens = new ArrayList<>();

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Carrinho(CarrinhoDTO dto) {
        this.id = dto.getId();
        this.precoTotal = dto.getPrecoTotal();
    }
}
