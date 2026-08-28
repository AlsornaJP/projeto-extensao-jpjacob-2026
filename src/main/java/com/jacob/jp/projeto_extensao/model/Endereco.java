package com.jacob.jp.projeto_extensao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="enderecos")
@NoArgsConstructor
@Getter
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="rua", nullable=false, length=255)
    private String rua;

    @Column(name="bairro", nullable=false, length=100)
    private String bairro;

    @Column(name="cidade", nullable=false, length=100)
    private String cidade;

    @Column(name="estado", nullable=false, length=50)
    private String estado;

    @Column(name="numero", nullable=false)
    private Integer numero;

    @Column(name="complemento", length=255)
    private String complemento;

    @Column(name="cep", nullable=false, length=50)
    private String cep;

    @Column(name="ativo", nullable=false)
    private Boolean ativo;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_cliente", referencedColumnName="id")
    private Cliente cliente;
}
