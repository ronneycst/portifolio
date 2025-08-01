package com.ronney.portfolio.exception;

public class MembroNotFoundException extends Exception {
        public MembroNotFoundException(Long id) {
            super("Membro não encontrado id: " + id);
        }
}
