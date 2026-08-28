package com.jacob.jp.projeto_extensao.dto;

import com.jacob.jp.projeto_extensao.model.Cliente;
import com.jacob.jp.projeto_extensao.model.Endereco;
import com.jacob.jp.projeto_extensao.model.Pedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ClienteDTO {

    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private Integer idCarrinho;
    private List<Integer> idsEnderecos;
    private List<Integer> idsPedidos;

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
        this.idCarrinho = cliente.getCarrinho() != null ? cliente.getCarrinho().getId() : null;
        this.idsEnderecos = cliente.getEnderecos() != null
                ? cliente.getEnderecos().stream().map(Endereco::getId).toList()
                : List.of();
        this.idsPedidos = cliente.getPedidos() != null
                ? cliente.getPedidos().stream().map(Pedido::getId).toList()
                : List.of();
    }
}
