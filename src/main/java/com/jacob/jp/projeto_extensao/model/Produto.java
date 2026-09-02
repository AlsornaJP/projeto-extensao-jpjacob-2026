package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.ProdutoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_fornecedor", referencedColumnName="id")
    private Fornecedor fornecedor;

    @OneToMany(mappedBy="produto", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderBy("preco ASC")
    private List<Variante> variantes = new ArrayList<>();

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Produto(ProdutoDTO dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
        this.descricao = dto.getDescricao();
    }

    public Produto(ProdutoDTO dto, Fornecedor fornecedor) {
        this(dto);
        this.fornecedor = fornecedor;
    }

    public void atualizarDados(String nome, String descricao, Fornecedor fornecedor) {
        this.nome = nome;
        this.descricao = descricao;
        this.fornecedor = fornecedor;
    }

    public void adicionarVariante(Variante variante) {
        this.variantes.add(variante);
    }

    public void removerVariante(Variante variante) {
        this.variantes.remove(variante);
    }
}
