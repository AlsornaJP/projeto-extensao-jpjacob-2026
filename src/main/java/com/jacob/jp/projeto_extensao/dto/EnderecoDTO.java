package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Endereco;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EnderecoDTO {

    private Integer id;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private Integer numero;
    private String complemento;
    private String cep;
    private Boolean ativo;
    private Integer idCliente;

    public EnderecoDTO(Endereco endereco) {
        this.id = endereco.getId();
        this.rua = endereco.getRua();
        this.bairro = endereco.getBairro();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.numero = endereco.getNumero();
        this.complemento = endereco.getComplemento();
        this.cep = endereco.getCep();
        this.ativo = endereco.getAtivo();
        this.idCliente = endereco.getCliente() != null ? endereco.getCliente().getId() : null;
    }
}
