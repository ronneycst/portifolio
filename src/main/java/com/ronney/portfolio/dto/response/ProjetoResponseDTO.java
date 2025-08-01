package com.ronney.portfolio.dto.response;

import com.ronney.portfolio.enums.RiscoProjeto;
import com.ronney.portfolio.enums.StatusProjeto;
import com.ronney.portfolio.model.Membro;
import com.ronney.portfolio.model.Pessoa;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ProjetoResponseDTO {
    private Long id;

    private String nome;

    private LocalDate dataInicio;

    private PessoaResponseDTO gerenteResponsavel;

    private LocalDate previsaoTermino;

    private LocalDate dataRealTermino;

    private BigDecimal orcamentoTotal;

    private String descricao;

    private StatusProjeto status;

    private RiscoProjeto risco;

    private List<MembroResponseDTO> membros;
}
