package com.ronney.portfolio.controller;

import com.ronney.portfolio.dto.request.MembroRequestDTO;
import com.ronney.portfolio.dto.response.MembroResponseDTO;
import com.ronney.portfolio.exception.MembroNotFoundException;
import com.ronney.portfolio.service.MembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/membros")
public class MembroController {

    @Autowired
    private MembroService membroService;

    @GetMapping
    public ResponseEntity<List<MembroResponseDTO>> findAll() {
        try {
            List<MembroResponseDTO> membroResponseDTOList = membroService.findAll();
            return ResponseEntity.ok(membroResponseDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroResponseDTO> findById(@PathVariable Long id) {
        try {
            Optional<MembroResponseDTO> membroResponseDTO = membroService.findById(id);
            return ResponseEntity.ok(membroResponseDTO.get());
        } catch (MembroNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<MembroResponseDTO> save(@RequestBody MembroRequestDTO membroRequestDTO) {
        try {
            Optional<MembroResponseDTO> membroResponseDTO = membroService.save(membroRequestDTO);
            return ResponseEntity.ok(membroResponseDTO.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<MembroResponseDTO> update(@RequestBody MembroRequestDTO membroRequestDTO) {
        try {
            Optional<MembroResponseDTO> membroResponseDTO = membroService.update(membroRequestDTO);
            return ResponseEntity.ok(membroResponseDTO.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<MembroResponseDTO> delete(@RequestBody MembroRequestDTO membroRequestDTO) {
        try {
            membroService.delete(membroRequestDTO.getId());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 