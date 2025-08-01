package com.ronney.portfolio.controller;

import com.ronney.portfolio.dto.request.PessoaRequestDTO;
import com.ronney.portfolio.dto.response.PessoaResponseDTO;
import com.ronney.portfolio.exception.PessoaNotFoundException;
import com.ronney.portfolio.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> findAll() {
        try {
            List<PessoaResponseDTO> pessoaResponseDTOList = pessoaService.findAll();
            return ResponseEntity.ok(pessoaResponseDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> findById(@PathVariable Long id) {
        try {
            Optional<PessoaResponseDTO> pessoaResponseDTO = pessoaService.findById(id);
            return ResponseEntity.ok(pessoaResponseDTO.get());
        } catch (PessoaNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> save(@RequestBody PessoaRequestDTO pessoaRequestDTO) {
        try {
            Optional<PessoaResponseDTO> pessoaResponseDTO = pessoaService.save(pessoaRequestDTO);
            return ResponseEntity.ok(pessoaResponseDTO.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<PessoaResponseDTO> update(@RequestBody PessoaRequestDTO pessoaRequestDTO) {
        try {
            Optional<PessoaResponseDTO> pessoaResponseDTO = pessoaService.update(pessoaRequestDTO);
            return ResponseEntity.ok(pessoaResponseDTO.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<PessoaResponseDTO> delete(@RequestBody PessoaRequestDTO pessoaRequestDTO) {
        try {
            pessoaService.delete(pessoaRequestDTO.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 