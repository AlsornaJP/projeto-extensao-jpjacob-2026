package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.ProdutoDTO;
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
    private Integer id;

    @Column(name="nome_produto", nullable=false, length=150)
    private String nome;

    @Column(name="descricao", nullable=false, length=500)
    private String descricao;

    @Column(name="preco", nullable=false, precision=10, scale=2)
    private BigDecimal preco;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_fornecedor", referencedColumnName="id")
    private Fornecedor fornecedor;

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Produto(ProdutoDTO dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
        this.descricao = dto.getDescricao();
        this.preco = dto.getPreco();
    }
}
