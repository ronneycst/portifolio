package com.ronney.portfolio.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProjetoRequestDTO {

    private Long id;

    private String nome;

    private LocalDate dataInicio;

    private Long gerenteResponsavelId;

    private LocalDate previsaoTermino;

    private LocalDate dataRealTermino;

    private BigDecimal orcamentoTotal;

    private String descricao;

    private String status;

    private String risco;

    private List<MembroRequestDTO> membros;
}
