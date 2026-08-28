package com.jacob.jp.projeto_extensao.model;

import com.jacob.jp.projeto_extensao.dto.EntregaDTO;
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
    private Integer id;

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

    // As associacoes vindas do DTO chegam como ids e sao resolvidas na camada de servico.

    public Entrega(EntregaDTO dto) {
        this.id = dto.getId();
        this.dataPostagem = dto.getDataPostagem();
        this.status = dto.getStatus();
    }
}
