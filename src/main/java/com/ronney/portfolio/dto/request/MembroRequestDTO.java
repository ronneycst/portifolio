package com.ronney.portfolio.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class MembroRequestDTO {
    private Long id;

    private Long projetoId;

    private Long pessoaId;

    private LocalDate dataInicio;

    private LocalDate dataFim;
}
