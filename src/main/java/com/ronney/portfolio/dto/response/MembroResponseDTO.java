package com.ronney.portfolio.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MembroResponseDTO {

    private Long id;

    private ProjetoResponseDTO projeto;

    private PessoaResponseDTO pessoa;

    private LocalDate dataInicio;

    private LocalDate dataFim;
}
