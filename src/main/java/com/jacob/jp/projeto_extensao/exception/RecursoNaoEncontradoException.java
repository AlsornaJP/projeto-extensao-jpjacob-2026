package com.jacob.jp.projeto_extensao.exception;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String recurso, Integer id) {
        this(recurso, id, "nao encontrado");
    }

    public RecursoNaoEncontradoException(String recurso, Integer id, String sufixo) {
        super(recurso + " " + id + " " + sufixo);
    }
}
