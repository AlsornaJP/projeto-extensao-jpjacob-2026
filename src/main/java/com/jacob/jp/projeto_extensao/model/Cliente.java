package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.ClienteDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="clientes")
@NoArgsConstructor
@Getter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="nome_cliente", nullable=false, length=150)
    private String nome;

    @Column(name="email",  nullable=false, length=150)
    private String email;

    @Column(name="telefone", nullable = false, length = 20, unique = true)
    private String telefone;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_carrinho",referencedColumnName = "id")
    private Carrinho carrinho;

    @OneToMany(mappedBy="cliente", cascade=CascadeType.ALL)
    private List<Endereco> enderecos = new ArrayList<>();

    @OneToMany(mappedBy="cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Cliente(ClienteDTO dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
        this.email = dto.getEmail();
        this.telefone = dto.getTelefone();
    }
}
