package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.EnderecoDTO;
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
    private Integer id;

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

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Endereco(EnderecoDTO dto) {
        this.id = dto.getId();
        this.rua = dto.getRua();
        this.bairro = dto.getBairro();
        this.cidade = dto.getCidade();
        this.estado = dto.getEstado();
        this.numero = dto.getNumero();
        this.complemento = dto.getComplemento();
        this.cep = dto.getCep();
        this.ativo = dto.getAtivo();
    }
}
