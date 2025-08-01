package com.ronney.portfolio.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PessoaRequestDTO {
    private Long id;

    private String nome;

    private LocalDate dataNascimento;

    private String cpf;

    private Boolean funcionario;

    private Boolean gerente;

    private List<ProjetoRequestDTO> projetosGerenciados;

    private List<MembroRequestDTO> membros;
}
