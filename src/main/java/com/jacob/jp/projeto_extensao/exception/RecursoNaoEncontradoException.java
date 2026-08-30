package com.jacob.jp.projeto_extensao.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Integer id) {
        super(recurso + " " + id + " nao encontrado");
    }
}
