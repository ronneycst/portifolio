package com.ronney.portfolio.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ronney.portfolio.enums.RiscoProjeto;
import com.ronney.portfolio.enums.StatusProjeto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "projeto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @NotNull(message = "Data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idgerente", nullable = false)
    @NotNull(message = "Gerente responsável é obrigatório")
    private Pessoa gerenteResponsavel;

    @Column(name = "data_previsao_fim")
    private LocalDate previsaoTermino;

    @Column(name = "data_fim")
    private LocalDate dataRealTermino;

    @DecimalMin(value = "0.0", message = "Orçamento deve ser maior ou igual a zero")
    @Column(name = "orcamento", precision = 15, scale = 2)
    private BigDecimal orcamentoTotal;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @NotNull(message = "Status é obrigatório")
    private StatusProjeto status = StatusProjeto.EM_ANALISE;

    @Enumerated(EnumType.STRING)
    @Column(name = "risco", nullable = false, length = 20)
    @NotNull(message = "Risco é obrigatório")
    private RiscoProjeto risco = RiscoProjeto.BAIXO;

    @JsonManagedReference("projeto-membros")
    @OneToMany(mappedBy = "projeto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Membro> membros = new ArrayList<>();


    public boolean podeSerExcluido() {
        return status.podeSerExcluido();
    }

    public boolean estaEmExecucao() {
        return status.estaEmExecucao();
    }

    public boolean estaFinalizado() {
        return status.estaFinalizado();
    }

    public boolean temRiscoAlto() {
        return risco == RiscoProjeto.ALTO;
    }

    public void adicionarMembro(Membro membro) {
        membros.add(membro);
        membro.setProjeto(this);
    }

    public void removerMembro(Membro membro) {
        membros.remove(membro);
        membro.setProjeto(null);
    }

    public boolean isAtrasado() {
        if (previsaoTermino == null || dataRealTermino != null || !status.estaEmExecucao()) {
            return false;
        }
        return LocalDate.now().isAfter(previsaoTermino);
    }

    public long getDiasAtraso() {
        if (!isAtrasado()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(previsaoTermino, LocalDate.now());
    }

    @Override
    public String toString() {
        return "Projeto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataInicio=" + dataInicio +
                ", gerenteResponsavel=" + (gerenteResponsavel != null ? gerenteResponsavel.getNome() : "null") +
                ", previsaoTermino=" + previsaoTermino +
                ", dataRealTermino=" + dataRealTermino +
                ", orcamentoTotal=" + orcamentoTotal +
                ", descricao='" + descricao + '\'' +
                ", status=" + status +
                ", risco=" + risco +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projeto projeto = (Projeto) o;
        return Objects.equals(id, projeto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 