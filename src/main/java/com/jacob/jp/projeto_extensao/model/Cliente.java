package com.jacob.jp.projeto_extensao.model;

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
    private Long id;

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
}
