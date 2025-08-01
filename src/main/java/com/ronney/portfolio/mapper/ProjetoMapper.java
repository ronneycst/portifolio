package com.ronney.portfolio.mapper;

import com.ronney.portfolio.dto.request.ProjetoRequestDTO;
import com.ronney.portfolio.dto.response.MembroResponseDTO;
import com.ronney.portfolio.dto.response.PessoaResponseDTO;
import com.ronney.portfolio.dto.response.ProjetoResponseDTO;
import com.ronney.portfolio.enums.RiscoProjeto;
import com.ronney.portfolio.enums.StatusProjeto;
import com.ronney.portfolio.exception.PessoaNotFoundException;
import com.ronney.portfolio.model.Membro;
import com.ronney.portfolio.model.Pessoa;
import com.ronney.portfolio.model.Projeto;
import com.ronney.portfolio.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjetoMapper {

    @Autowired
    private PessoaService pessoaService;

    public Projeto toEntity(ProjetoRequestDTO dto) {
        Projeto projeto = Projeto.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .dataInicio(dto.getDataInicio())
                .previsaoTermino(dto.getPrevisaoTermino())
                .dataRealTermino(dto.getDataRealTermino())
                .status(StatusProjeto.valueOf(dto.getStatus()))
                .orcamentoTotal(dto.getOrcamentoTotal())
                .descricao(dto.getDescricao())
                .risco(RiscoProjeto.valueOf(dto.getRisco()))
                .build();

        if (dto.getGerenteResponsavelId() != null) {
            pessoaService.findByIdEntity(dto.getGerenteResponsavelId()).ifPresent(projeto::setGerenteResponsavel);
        }

        return projeto;
    }

    public ProjetoResponseDTO toResponseDTO(Projeto projeto) {
        ProjetoResponseDTO dto = ProjetoResponseDTO.builder()
                .id(projeto.getId())
                .nome(projeto.getNome())
                .dataInicio(projeto.getDataInicio())
                .dataRealTermino(projeto.getDataRealTermino())
                .status(projeto.getStatus())
                .orcamentoTotal(projeto.getOrcamentoTotal())
                .descricao(projeto.getDescricao())
                .risco(projeto.getRisco())
                .previsaoTermino(projeto.getPrevisaoTermino())
                .build();

//        dto.setAtrasado(projeto.isAtrasado());
//        dto.setDiasAtraso(projeto.getDiasAtraso());

        if (projeto.getGerenteResponsavel() != null) {
            dto.setGerenteResponsavel(toGerenteResponseDTO(projeto.getGerenteResponsavel()));
        }

        if (projeto.getMembros() != null) {
            dto.setMembros(projeto.getMembros().stream()
                    .map(this::toMembroResponseDTO)
                    .toList());
        }

        return dto;
    }

    public List<ProjetoResponseDTO> toResponseDTOList(List<Projeto> projetos) {
        return projetos.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private PessoaResponseDTO toGerenteResponseDTO(Pessoa pessoa) {
        return PessoaResponseDTO.builder()
                .id(pessoa.getId())
                .nome(pessoa.getNome())
                .gerente(pessoa.getGerente())
                .funcionario(pessoa.getFuncionario())
                .cpf(pessoa.getCpf())
                .dataNascimento(pessoa.getDataNascimento())
                .build();
    }

    private MembroResponseDTO toMembroResponseDTO(Membro membro) {
        return MembroResponseDTO.builder()
                .id(membro.getId())
                .dataInicio(membro.getDataInicio())
                .dataFim(membro.getDataFim())
//                .projeto(toResponseDTO(membro.getProjeto()))
                .build();
    }

    public void merge(Projeto projeto, ProjetoRequestDTO dto) {
        projeto.setNome(dto.getNome());
        projeto.setDataInicio(dto.getDataInicio());
        projeto.setPrevisaoTermino(dto.getPrevisaoTermino());
        projeto.setDataRealTermino(dto.getDataRealTermino());
        projeto.setStatus(StatusProjeto.valueOf(dto.getStatus()));
        projeto.setOrcamentoTotal(dto.getOrcamentoTotal());
        projeto.setDescricao(dto.getDescricao());
        projeto.setRisco(RiscoProjeto.valueOf(dto.getRisco()));

        if (dto.getGerenteResponsavelId() != null) {
            pessoaService.findByIdEntity(dto.getGerenteResponsavelId())
                    .ifPresent(projeto::setGerenteResponsavel);
        }
    }
}