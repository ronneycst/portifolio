package com.ronney.portfolio.enums;

import lombok.Getter;

@Getter
public enum StatusProjeto {
    EM_ANALISE("em análise"),
    ANALISE_REALIZADA("análise realizada"),
    ANALISE_APROVADA("análise aprovada"),
    INICIADO("iniciado"),
    PLANEJADO("planejado"),
    EM_ANDAMENTO("em andamento"),
    ENCERRADO("encerrado"),
    CANCELADO("cancelado");

    private final String descricao;

    StatusProjeto(String descricao) {
        this.descricao = descricao;
    }

    public static StatusProjeto fromDescricao(String descricao) {
        for (StatusProjeto status : values()) {
            if (status.descricao.equalsIgnoreCase(descricao)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + descricao);
    }

    public boolean podeSerExcluido() {
        return this == EM_ANALISE || 
               this == ANALISE_REALIZADA || 
               this == ANALISE_APROVADA || 
               this == PLANEJADO || 
               this == CANCELADO;
    }

    public boolean estaEmExecucao() {
        return this == INICIADO || this == EM_ANDAMENTO;
    }

    public boolean estaFinalizado() {
        return this == ENCERRADO || this == CANCELADO;
    }
} 