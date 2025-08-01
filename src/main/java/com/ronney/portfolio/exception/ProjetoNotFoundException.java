package com.ronney.portfolio.exception;

public class ProjetoNotFoundException extends Exception {
    public ProjetoNotFoundException(Long id) {
        super("Projeto não encontrado id: " + id);
    }
}
