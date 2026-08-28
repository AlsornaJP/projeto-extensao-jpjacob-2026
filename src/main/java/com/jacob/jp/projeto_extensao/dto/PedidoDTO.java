package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Pedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class PedidoDTO {

    private Integer id;
    private LocalDate dataPedido;
    private LocalTime horaPedido;
    private Integer idCliente;
    private Integer idCarrinho;

    public PedidoDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.dataPedido = pedido.getDataPedido();
        this.horaPedido = pedido.getHoraPedido();
        this.idCliente = pedido.getCliente() != null ? pedido.getCliente().getId() : null;
        this.idCarrinho = pedido.getCarrinho() != null ? pedido.getCarrinho().getId() : null;
    }
}
