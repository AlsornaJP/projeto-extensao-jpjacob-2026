package com.jacob.jp.projeto_extensao.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="pedidos")
@NoArgsConstructor
@Getter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="data_pedido", nullable=false)
    private LocalDate dataPedido;

    @Column(name="hora_pedido", nullable=false)
    private LocalTime horaPedido;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_cliente", referencedColumnName="id")
    private Cliente cliente;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="id_carrinho", referencedColumnName="id")
    private Carrinho carrinho;
}
