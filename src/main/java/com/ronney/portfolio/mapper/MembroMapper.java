package com.ronney.portfolio.mapper;

import com.ronney.portfolio.dto.request.MembroRequestDTO;
import com.ronney.portfolio.dto.response.MembroResponseDTO;
import com.ronney.portfolio.dto.response.PessoaResponseDTO;
import com.ronney.portfolio.model.Membro;
import com.ronney.portfolio.model.Pessoa;
import com.ronney.portfolio.model.Projeto;
import com.ronney.portfolio.service.PessoaService;
import com.ronney.portfolio.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MembroMapper {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private PessoaMapper pessoaMapper;
    @Autowired
    private ProjetoMapper projetoMapper;

    public Membro toEntity(MembroRequestDTO dto) throws Exception {
        Membro membro = Membro.builder()
                .id(dto.getId())
                .dataInicio(dto.getDataInicio())
                .dataFim(dto.getDataFim())
                .build();

        if (dto.getPessoaId() != null) {
            Optional<PessoaResponseDTO> pessoaResponseDTOOptional = pessoaService.findById(dto.getPessoaId());
            pessoaResponseDTOOptional.ifPresent(pessoaResponseDTO -> membro.setPessoa(Pessoa.builder()
                    .id(pessoaResponseDTO.getId())
                    .cpf(pessoaResponseDTO.getCpf())
                    .nome(pessoaResponseDTO.getNome())
                    .dataNascimento(pessoaResponseDTO.getDataNascimento())
                    .membros(pessoaResponseDTO.getMembros())
                    .funcionario(pessoaResponseDTO.getFuncionario())
                    .gerente(pessoaResponseDTO.getGerente())
                    .projetosGerenciados(pessoaResponseDTO.getProjetosGerenciados())
                    .build()));
        }

        if (dto.getProjetoId() != null) {
            projetoService.findById(dto.getProjetoId()).ifPresent(projetoResponseDTO -> {
                membro.setProjeto(Projeto.builder()
                                .risco(projetoResponseDTO.getRisco())
                                .id(projetoResponseDTO.getId())
                                .orcamentoTotal(projetoResponseDTO.getOrcamentoTotal())
                                .dataRealTermino(projetoResponseDTO.getDataRealTermino())
                                .previsaoTermino(projetoResponseDTO.getPrevisaoTermino())
                                .status(projetoResponseDTO.getStatus())
                                .descricao(projetoResponseDTO.getDescricao())
                                .dataInicio(projetoResponseDTO.getDataInicio())
                        .build());
            });
        }

        return membro;
    }

    public MembroResponseDTO toResponseDTO(Membro membro) {
        return MembroResponseDTO.builder()
                .id(membro.getId())
                .dataInicio(membro.getDataInicio())
                .dataFim(membro.getDataFim())
                .pessoa(pessoaMapper.toResponseDTO(membro.getPessoa()))
                .projeto(projetoMapper.toResponseDTO(membro.getProjeto()))
                .build();
    }

    public List<MembroResponseDTO> toResponseDTOList(List<Membro> membros) {
        return membros.stream()
                .map(this::toResponseDTO)
                .toList();
    }
} 