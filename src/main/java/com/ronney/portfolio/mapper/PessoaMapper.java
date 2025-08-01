package com.ronney.portfolio.mapper;

import com.ronney.portfolio.dto.request.PessoaRequestDTO;
import com.ronney.portfolio.dto.response.PessoaResponseDTO;
import com.ronney.portfolio.model.Pessoa;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PessoaMapper {

    public Pessoa toEntity(PessoaRequestDTO dto) {
        return Pessoa.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .dataNascimento(dto.getDataNascimento())
                .funcionario(dto.getFuncionario())
                .gerente(dto.getGerente())
                .build();
    }

    public PessoaResponseDTO toResponseDTO(Pessoa pessoa) {
        return PessoaResponseDTO.builder()
                .id(pessoa.getId())
                .nome(pessoa.getNome())
                .cpf(pessoa.getCpf())
                .dataNascimento(pessoa.getDataNascimento())
                .funcionario(pessoa.getFuncionario())
                .gerente(pessoa.getGerente())
                .build();
    }

    public List<PessoaResponseDTO> toResponseDTOList(List<Pessoa> pessoas) {
        return pessoas.stream()
                .map(this::toResponseDTO)
                .toList();
    }
} 