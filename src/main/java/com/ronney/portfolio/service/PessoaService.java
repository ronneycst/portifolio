package com.ronney.portfolio.service;

import com.ronney.portfolio.dto.request.PessoaRequestDTO;
import com.ronney.portfolio.dto.response.PessoaResponseDTO;
import com.ronney.portfolio.exception.PessoaNotFoundException;
import com.ronney.portfolio.mapper.PessoaMapper;
import com.ronney.portfolio.model.Pessoa;
import com.ronney.portfolio.repository.PessoaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PessoaMapper pessoaMapper;

    public Optional<PessoaResponseDTO> findById(Long id) throws Exception {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (pessoaOptional.isEmpty()) {
            log.warn("Pessoa {} not found", id);
            throw new PessoaNotFoundException(id);
        }
        log.debug("Pessoa encontrada: {}", pessoaOptional.get());
        PessoaResponseDTO pessoaResponseDTO = pessoaMapper.toResponseDTO(pessoaOptional.get());
        return Optional.of(pessoaResponseDTO);
    }

    public List<PessoaResponseDTO> findAll() {
        log.debug("Buscando todas as pessoas");

        List<Pessoa> pessoas = pessoaRepository.findAll();

        log.debug("Pessoas encontradas: {}", pessoas);

        return pessoaMapper.toResponseDTOList(pessoas);
    }

    public Optional<PessoaResponseDTO> save(PessoaRequestDTO pessoaRequestDTO) throws Exception {
        log.debug("Iniciando a criação da pessoa: {}", pessoaRequestDTO);

        Pessoa pessoaToSave = pessoaMapper.toEntity(pessoaRequestDTO);

        pessoaToSave = pessoaRepository.save(pessoaToSave);

        log.debug("Pessoa salva com sucesso: {}", pessoaToSave);

        PessoaResponseDTO pessoaResponseDTO = pessoaMapper.toResponseDTO(pessoaToSave);

        return Optional.of(pessoaResponseDTO);
    }

    public Optional<PessoaResponseDTO> update(PessoaRequestDTO pessoaRequestDTO) throws Exception {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaRequestDTO.getId());
        if (pessoaOptional.isEmpty()) {
            log.warn("Pessoa {} not found", pessoaRequestDTO.getId());
            throw new PessoaNotFoundException(pessoaRequestDTO.getId());
        }
        log.debug("Pessoa encontrada: {}", pessoaOptional.get());

        Pessoa pessoaToUpdate = pessoaMapper.toEntity(pessoaRequestDTO);

        pessoaToUpdate = pessoaRepository.save(pessoaToUpdate);

        log.debug("Pessoa atualizada com sucesso!");

        PessoaResponseDTO responseDTO = pessoaMapper.toResponseDTO(pessoaToUpdate);

        return Optional.of(responseDTO);
    }

    public void delete(Long id) throws Exception {
        log.info("Iniciando exclusão id: {}", id);

        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);

        if (pessoaOptional.isEmpty()) {
            log.warn("Pessoa {} não encontrada", id);
            throw new PessoaNotFoundException(id);
        }

        pessoaRepository.deleteById(pessoaOptional.get().getId());

        log.debug("Pessoa removida com sucesso!");
    }

    // Método para compatibilidade com outros services que precisam da entidade
    public Optional<Pessoa> findByIdEntity(Long id) {
        return pessoaRepository.findById(id);
    }
} 