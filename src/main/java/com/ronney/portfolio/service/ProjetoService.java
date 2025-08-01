package com.ronney.portfolio.service;

import com.ronney.portfolio.dto.request.ProjetoRequestDTO;
import com.ronney.portfolio.dto.response.ProjetoResponseDTO;
import com.ronney.portfolio.exception.PessoaNotFoundException;
import com.ronney.portfolio.exception.ProjectDeletionNotAllowedException;
import com.ronney.portfolio.exception.ProjetoNotFoundException;
import com.ronney.portfolio.mapper.ProjetoMapper;
import com.ronney.portfolio.model.Projeto;
import com.ronney.portfolio.repository.ProjetoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ProjetoMapper projetoMapper;

    public Optional<ProjetoResponseDTO> findById(Long id) throws Exception {
        Optional<Projeto> projetoOptional =  projetoRepository.findById(id);
        if(projetoOptional.isEmpty()) {
            log.warn("Project {} not found", id);
            throw new ProjetoNotFoundException(id);
        }
        log.debug("Projeto encontrado: {}", projetoOptional.get());
        ProjetoResponseDTO projetoResponseDTO = projetoMapper.toResponseDTO(projetoOptional.get());
        return Optional.of(projetoResponseDTO);
    }

    public List<ProjetoResponseDTO> findAll() {
        log.debug("Buscando todos os projetos");

        List<Projeto> projetos = projetoRepository.findAll();

        log.debug("Projetos encontrados: {}", projetos);

        return projetoMapper.toResponseDTOList(projetos);
    }

    public Optional<ProjetoResponseDTO> save(ProjetoRequestDTO projetoRequestDTO) throws Exception {
        log.debug("Iniciando a criação do projeto: {}", projetoRequestDTO);

        Projeto projetoToSave = projetoMapper.toEntity(projetoRequestDTO);

        projetoToSave = projetoRepository.save(projetoToSave);

        log.debug("Projeto salvo com sucesso: {}", projetoToSave);

        ProjetoResponseDTO projetoResponseDTO = projetoMapper.toResponseDTO(projetoToSave);

        return Optional.of(projetoResponseDTO);
    }

    public Optional<ProjetoResponseDTO> update(ProjetoRequestDTO projetoRequestDTO) throws Exception {
        Optional<Projeto> projetoOptional =  projetoRepository.findById(projetoRequestDTO.getId());
        if(projetoOptional.isEmpty()) {
            log.warn("Project {} not found", projetoRequestDTO.getId());
            throw new ProjetoNotFoundException(projetoRequestDTO.getId());
        }
        log.debug("Projeto encontrado: {}", projetoOptional.get());

        Projeto projetoToUpdate = projetoOptional.get();
        projetoMapper.merge(projetoToUpdate, projetoRequestDTO);
        projetoRepository.save(projetoToUpdate);

        log.debug("Projeto atualizado com sucesso!");

        ProjetoResponseDTO responseDTO = projetoMapper.toResponseDTO(projetoToUpdate);

        return Optional.of(responseDTO);
    }

    public void delete(Long id) throws Exception {
        log.info("Iniciando exclusão do projeto: {}", id);

        Optional<Projeto> projetoOptional = projetoRepository.findById(id);

        if(projetoOptional.isEmpty()) {
            log.warn("Projeto {} não encontrado", id);
            throw new ProjetoNotFoundException(id);
        }
        Projeto projeto = projetoOptional.get();

        if(!projeto.podeSerExcluido()){
            log.error("Projeto não pode ser excluido | status: {}", projeto.getStatus().getDescricao());
            throw new ProjectDeletionNotAllowedException("Projeto não pode ser excluido | status: " + projeto.getStatus().getDescricao());
        }

        projetoRepository.deleteById(id);

        log.debug("Projeto removido com sucesso!");
    }
} 