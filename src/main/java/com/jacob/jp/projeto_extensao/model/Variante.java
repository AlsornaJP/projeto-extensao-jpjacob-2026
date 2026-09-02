package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.VarianteDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="variantes")
@NoArgsConstructor
@Getter
public class Variante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="medida", nullable=false, length=50)
    private String medida;

    @Column(name="preco", nullable=false, precision=10, scale=2)
    private BigDecimal preco;

    @Column(name="estoque", nullable=false)
    private Integer estoque;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_produto", referencedColumnName="id")
    private Produto produto;

    public Variante(VarianteDTO dto) {
        this.id = dto.getId();
        this.medida = dto.getMedida();
        this.preco = dto.getPreco();
        this.estoque = dto.getEstoque() != null ? dto.getEstoque() : 0;
    }

    public Variante(VarianteDTO dto, Produto produto) {
        this(dto);
        this.produto = produto;
    }

    public void atualizarDados(String medida, BigDecimal preco) {
        this.medida = medida;
        this.preco = preco;
    }

    public void baixarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a baixar deve ser positiva");
        }
        if (this.estoque < quantidade) {
            throw new EstoqueInsuficiente(this.estoque, quantidade);
        }
        this.estoque -= quantidade;
    }

    public void reporEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade a repor deve ser positiva");
        }
        this.estoque += quantidade;
    }

    public static class EstoqueInsuficiente extends RuntimeException {
        public EstoqueInsuficiente(int disponivel, int solicitado) {
            super("Estoque insuficiente: disponivel " + disponivel + ", solicitado " + solicitado);
        }
    }
}
