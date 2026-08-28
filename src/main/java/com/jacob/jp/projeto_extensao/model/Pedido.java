package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.PedidoDTO;
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
    private Integer id;

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

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Pedido(PedidoDTO dto) {
        this.id = dto.getId();
        this.dataPedido = dto.getDataPedido();
        this.horaPedido = dto.getHoraPedido();
    }
}
