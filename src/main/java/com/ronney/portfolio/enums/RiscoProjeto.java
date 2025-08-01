package com.ronney.portfolio.enums;

import lombok.Getter;

@Getter
public enum RiscoProjeto {
    BAIXO("baixo"),
    MEDIO("médio"),
    ALTO("alto");

    private final String descricao;

    RiscoProjeto(String descricao) {
        this.descricao = descricao;
    }

    public static RiscoProjeto fromDescricao(String descricao) {
        for (RiscoProjeto risco : values()) {
            if (risco.descricao.equalsIgnoreCase(descricao)) {
                return risco;
            }
        }
        throw new IllegalArgumentException("Risco inválido: " + descricao);
    }

    public int getNivel() {
        switch (this) {
            case BAIXO: return 1;
            case MEDIO: return 2;
            case ALTO: return 3;
            default: return 0;
        }
    }

    public boolean isAlto() {
        return this == ALTO;
    }

    public boolean isMedio() {
        return this == MEDIO;
    }

    public boolean isBaixo() {
        return this == BAIXO;
    }
} 