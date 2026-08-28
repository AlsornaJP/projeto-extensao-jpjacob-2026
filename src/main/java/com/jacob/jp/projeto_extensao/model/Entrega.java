package com.jacob.jp.projeto_extensao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="entregas")
@NoArgsConstructor
@Getter
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="data_postagem", nullable=false)
    private LocalDate dataPostagem;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false, length=50)
    private StatusEntrega status;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_pedido", referencedColumnName="id")
    private Pedido pedido;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_endereco", referencedColumnName="id")
    private Endereco endereco;
}
