package com.ronney.portfolio.service;

import com.ronney.portfolio.dto.request.MembroRequestDTO;
import com.ronney.portfolio.dto.response.MembroResponseDTO;
import com.ronney.portfolio.exception.MembroNotFoundException;
import com.ronney.portfolio.mapper.MembroMapper;
import com.ronney.portfolio.model.Membro;
import com.ronney.portfolio.repository.MembroRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class MembroService {

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private MembroMapper membroMapper;

    public Optional<MembroResponseDTO> findById(Long id) throws Exception {
        Optional<Membro> membroOptional = membroRepository.findById(id);
        if (membroOptional.isEmpty()) {
            log.warn("Membro {} not found", id);
            throw new MembroNotFoundException(id);
        }
        log.debug("Membro encontrado: {}", membroOptional.get());
        MembroResponseDTO membroResponseDTO = membroMapper.toResponseDTO(membroOptional.get());
        return Optional.of(membroResponseDTO);
    }

    public List<MembroResponseDTO> findAll() {
        log.debug("Buscando todos os membros");

        List<Membro> membros = membroRepository.findAll();

        log.debug("Membros encontrados: {}", membros);

        return membroMapper.toResponseDTOList(membros);
    }

    public Optional<MembroResponseDTO> save(MembroRequestDTO membroRequestDTO) throws Exception {
        log.debug("Iniciando a criação do membro: {}", membroRequestDTO);

        Membro membroToSave = membroMapper.toEntity(membroRequestDTO);

        membroToSave = membroRepository.save(membroToSave);

        log.debug("Membro salvo com sucesso: {}", membroToSave);

        MembroResponseDTO membroResponseDTO = membroMapper.toResponseDTO(membroToSave);

        return Optional.of(membroResponseDTO);
    }

    public Optional<MembroResponseDTO> update(MembroRequestDTO membroRequestDTO) throws Exception {
        Optional<Membro> membroOptional = membroRepository.findById(membroRequestDTO.getId());
        if (membroOptional.isEmpty()) {
            log.warn("Membro {} not found", membroRequestDTO.getId());
            throw new MembroNotFoundException(membroRequestDTO.getId());
        }
        log.debug("Membro encontrado: {}", membroOptional.get());

        Membro membroToUpdate = membroMapper.toEntity(membroRequestDTO);

        membroToUpdate = membroRepository.save(membroToUpdate);

        log.debug("Membro atualizado com sucesso!");

        MembroResponseDTO responseDTO = membroMapper.toResponseDTO(membroToUpdate);

        return Optional.of(responseDTO);
    }

    public void delete(Long id) throws Exception {
        log.info("Iniciando exclusão do membro: {}", id);

        Optional<Membro> membroOptional = membroRepository.findById(id);

        if (membroOptional.isEmpty()) {
            log.warn("Membro {} não encontrado", id);
            throw new MembroNotFoundException(id);
        }

        membroRepository.deleteById(id);

        log.debug("Membro removido com sucesso!");
    }
}