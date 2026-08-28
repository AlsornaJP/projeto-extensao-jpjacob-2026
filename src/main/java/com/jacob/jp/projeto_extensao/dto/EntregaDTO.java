package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Entrega;
import com.jacob.jp.projeto_extensao.model.StatusEntrega;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class EntregaDTO {

    private Integer id;
    private LocalDate dataPostagem;
    private StatusEntrega status;
    private Integer idPedido;
    private Integer idEndereco;

    public EntregaDTO(Entrega entrega) {
        this.id = entrega.getId();
        this.dataPostagem = entrega.getDataPostagem();
        this.status = entrega.getStatus();
        this.idPedido = entrega.getPedido() != null ? entrega.getPedido().getId() : null;
        this.idEndereco = entrega.getEndereco() != null ? entrega.getEndereco().getId() : null;
    }
}
