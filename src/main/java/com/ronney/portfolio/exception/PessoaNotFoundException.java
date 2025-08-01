package com.ronney.portfolio.exception;

public class PessoaNotFoundException extends Exception {
    public PessoaNotFoundException(Long id) {
        super("Pessoa não encontrada id: " + id);
    }
}
