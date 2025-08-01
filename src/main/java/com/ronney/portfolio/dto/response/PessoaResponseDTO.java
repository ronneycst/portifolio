package com.ronney.portfolio.dto.response;

import com.ronney.portfolio.model.Membro;
import com.ronney.portfolio.model.Projeto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PessoaResponseDTO {
    private Long id;

    private String nome;

    private LocalDate dataNascimento;

    private String cpf;

    private Boolean funcionario;

    private Boolean gerente;

    private List<Projeto> projetosGerenciados;

    private List<Membro> membros;
}
