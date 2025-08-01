package com.ronney.portfolio.controller;

import com.ronney.portfolio.dto.request.ProjetoRequestDTO;
import com.ronney.portfolio.dto.response.ProjetoResponseDTO;
import com.ronney.portfolio.exception.ProjetoNotFoundException;
import com.ronney.portfolio.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @GetMapping
    public ResponseEntity<List<ProjetoResponseDTO>> findAll() {
        try {
            List<ProjetoResponseDTO> projetoResponseDTOList = projetoService.findAll();
            return ResponseEntity.ok(projetoResponseDTOList);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> findById(@PathVariable Long id) {
        try {
            Optional<ProjetoResponseDTO> projetoResponseDTO = projetoService.findById(id);
            return ResponseEntity.ok(projetoResponseDTO.get());
        }catch (ProjetoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> save(@RequestBody ProjetoRequestDTO projetoRequestDTO) {
        try {
            Optional<ProjetoResponseDTO> projetoResponseDTO = projetoService.save(projetoRequestDTO);
            return ResponseEntity.ok(projetoResponseDTO.get());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<ProjetoResponseDTO> update(@RequestBody ProjetoRequestDTO projetoRequestDTO) {
        try {
            Optional<ProjetoResponseDTO> projetoResponseDTO = projetoService.update(projetoRequestDTO);
            return ResponseEntity.ok(projetoResponseDTO.get());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<ProjetoResponseDTO> delete(@RequestBody ProjetoRequestDTO projetoRequestDTO) {
        try {
            projetoService.delete(projetoRequestDTO.getId());
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 