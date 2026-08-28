package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.ItemDTO;
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
    private Integer id;

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

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Item(ItemDTO dto) {
        this.id = dto.getId();
        this.quantidade = dto.getQuantidade();
        this.preco = dto.getPreco();
    }
}
