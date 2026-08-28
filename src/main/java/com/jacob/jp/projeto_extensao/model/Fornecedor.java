package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.FornecedorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="fornecedores")
@NoArgsConstructor
@Getter
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="nome_fornecedor", nullable=false, length=150)
    private String nome;

    @OneToMany(mappedBy="fornecedor")
    private List<Produto> produtos = new ArrayList<>();

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Fornecedor(FornecedorDTO dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
    }
}
