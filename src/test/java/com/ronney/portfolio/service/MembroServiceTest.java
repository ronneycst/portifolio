package com.ronney.portfolio.service;

import com.ronney.portfolio.dto.request.MembroRequestDTO;
import com.ronney.portfolio.dto.response.MembroResponseDTO;
import com.ronney.portfolio.dto.response.PessoaResponseDTO;
import com.ronney.portfolio.dto.response.ProjetoResponseDTO;
import com.ronney.portfolio.enums.StatusProjeto;
import com.ronney.portfolio.exception.MembroNotFoundException;
import com.ronney.portfolio.mapper.MembroMapper;
import com.ronney.portfolio.model.Membro;
import com.ronney.portfolio.model.Pessoa;
import com.ronney.portfolio.model.Projeto;
import com.ronney.portfolio.repository.MembroRepository;
import com.ronney.portfolio.service.PessoaService;
import com.ronney.portfolio.service.ProjetoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para MembroService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do MembroService")
class MembroServiceTest {

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private MembroMapper membroMapper;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private ProjetoService projetoService;

    @InjectMocks
    private MembroService membroService;

    private Membro membro1;
    private Membro membro2;
    private MembroRequestDTO membroRequestDTO;
    private MembroResponseDTO membroResponseDTO;
    private Pessoa pessoa;
    private Projeto projeto;

    @BeforeEach
    void setUp() {
        pessoa = Pessoa.builder()
                .id(1L)
                .nome("João Silva")
                .funcionario(true)
                .gerente(true)
                .build();

        projeto = Projeto.builder()
                .id(1L)
                .nome("Sistema de Gestão Financeira")
                .dataInicio(LocalDate.of(2024, 1, 15))
                .status(StatusProjeto.EM_ANDAMENTO)
                .build();

        membro1 = Membro.builder()
                .id(1L)
                .pessoa(pessoa)
                .projeto(projeto)
                .dataInicio(LocalDate.of(2024, 1, 15))
                .dataFim(null)
                .build();

        membro2 = Membro.builder()
                .id(2L)
                .pessoa(pessoa)
                .projeto(projeto)
                .dataInicio(LocalDate.of(2024, 2, 1))
                .dataFim(LocalDate.of(2024, 6, 30))
                .build();

        membroRequestDTO = MembroRequestDTO.builder()
                .pessoaId(1L)
                .projetoId(1L)
                .dataInicio(LocalDate.of(2024, 1, 15))
                .dataFim(null)
                .build();

        membroResponseDTO = MembroResponseDTO.builder()
                .id(1L)
                .pessoa(PessoaResponseDTO.builder().id(1L).nome("João Silva").build())
                .projeto(ProjetoResponseDTO.builder().id(1L).nome("Sistema de Gestão Financeira").build())
                .dataInicio(LocalDate.of(2024, 1, 15))
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os membros")
    void deveListarTodosOsMembros() {
        // Given
        List<Membro> membros = Arrays.asList(membro1, membro2);
        List<MembroResponseDTO> expectedResponse = Arrays.asList(membroResponseDTO, membroResponseDTO);
        
        when(membroRepository.findAll()).thenReturn(membros);
        when(membroMapper.toResponseDTOList(membros)).thenReturn(expectedResponse);

        // When
        List<MembroResponseDTO> result = membroService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(membroRepository).findAll();
        verify(membroMapper).toResponseDTOList(membros);
    }

    @Test
    @DisplayName("Deve buscar membro por ID")
    void deveBuscarMembroPorId() throws Exception {
        // Given
        Long id = 1L;
        when(membroRepository.findById(id)).thenReturn(Optional.of(membro1));
        when(membroMapper.toResponseDTO(membro1)).thenReturn(membroResponseDTO);

        // When
        Optional<MembroResponseDTO> result = membroService.findById(id);

        // Then
        assertNotNull(result.get());
        assertEquals(membroResponseDTO, result.get());
        verify(membroRepository).findById(id);
        verify(membroMapper).toResponseDTO(membro1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando membro não existe")
    void deveLancarExcecaoQuandoMembroNaoExiste() {
        // Given
        Long id = 999L;
        when(membroRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MembroNotFoundException.class, () -> membroService.findById(id));
        verify(membroRepository).findById(id);
        verify(membroMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve salvar um novo membro")
    void deveSalvarUmNovoMembro() throws Exception {
        // Given
        when(membroMapper.toEntity(membroRequestDTO)).thenReturn(membro1);
        when(membroRepository.save(any(Membro.class))).thenReturn(membro1);
        when(membroMapper.toResponseDTO(membro1)).thenReturn(membroResponseDTO);

        // When
        Optional<MembroResponseDTO> result = membroService.save(membroRequestDTO);

        // Then
        assertNotNull(result.get());
        assertEquals(membroResponseDTO, result.get());
        verify(membroMapper).toEntity(membroRequestDTO);
        verify(membroRepository).save(any(Membro.class));
        verify(membroMapper).toResponseDTO(membro1);
    }

    @Test
    @DisplayName("Deve atualizar um membro existente")
    void deveAtualizarUmMembroExistente() throws Exception {
        // Given
        membroRequestDTO.setId(1L);
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro1));
        when(membroMapper.toEntity(membroRequestDTO)).thenReturn(membro1);
        when(membroRepository.save(any(Membro.class))).thenReturn(membro1);
        when(membroMapper.toResponseDTO(membro1)).thenReturn(membroResponseDTO);

        // When
        Optional<MembroResponseDTO> result = membroService.update(membroRequestDTO);

        // Then
        assertNotNull(result.get());
        assertEquals(membroResponseDTO, result.get());
        verify(membroRepository).findById(1L);
        verify(membroMapper).toEntity(membroRequestDTO);
        verify(membroRepository).save(any(Membro.class));
        verify(membroMapper).toResponseDTO(membro1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar membro inexistente")
    void deveLancarExcecaoAoAtualizarMembroInexistente() {
        // Given
        membroRequestDTO.setId(999L);
        when(membroRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MembroNotFoundException.class, () -> membroService.update(membroRequestDTO));
        verify(membroRepository).findById(999L);
        verify(membroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar um membro")
    void deveDeletarUmMembro() throws Exception {
        // Given
        Long id = 1L;
        when(membroRepository.findById(id)).thenReturn(Optional.of(membro1));
        doNothing().when(membroRepository).deleteById(id);

        // When
        membroService.delete(id);

        // Then
        verify(membroRepository).findById(id);
        verify(membroRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar membro inexistente")
    void deveLancarExcecaoAoDeletarMembroInexistente() {
        // Given
        Long id = 999L;
        when(membroRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MembroNotFoundException.class, () -> membroService.delete(id));
        verify(membroRepository).findById(id);
        verify(membroRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há membros")
    void deveRetornarListaVaziaQuandoNaoHaMembros() {
        // Given
        List<Membro> membrosVazios = Arrays.asList();
        List<MembroResponseDTO> responseVazio = Arrays.asList();
        when(membroRepository.findAll()).thenReturn(membrosVazios);
        when(membroMapper.toResponseDTOList(membrosVazios)).thenReturn(responseVazio);

        // When
        List<MembroResponseDTO> result = membroService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(membroRepository).findAll();
        verify(membroMapper).toResponseDTOList(membrosVazios);
    }

    @Test
    @DisplayName("Deve salvar membro com dados mínimos")
    void deveSalvarMembroComDadosMinimos() throws Exception {
        // Given
        MembroRequestDTO requestMinimo = MembroRequestDTO.builder()
                .pessoaId(1L)
                .projetoId(1L)
                .dataInicio(LocalDate.of(2024, 1, 1))
                .build();

        Membro membroMinimo = Membro.builder()
                .pessoa(pessoa)
                .projeto(projeto)
                .dataInicio(LocalDate.of(2024, 1, 1))
                .build();

        MembroResponseDTO responseMinimo = MembroResponseDTO.builder()
                .pessoa(PessoaResponseDTO.builder().id(1L).nome("João Silva").build())
                .projeto(ProjetoResponseDTO.builder().id(1L).nome("Sistema de Gestão Financeira").build())
                .dataInicio(LocalDate.of(2024, 1, 1))
                .build();

        when(membroMapper.toEntity(requestMinimo)).thenReturn(membroMinimo);
        when(membroRepository.save(any(Membro.class))).thenReturn(membroMinimo);
        when(membroMapper.toResponseDTO(membroMinimo)).thenReturn(responseMinimo);

        // When
        Optional<MembroResponseDTO> result = membroService.save(requestMinimo);

        // Then
        assertNotNull(result.get());
        assertEquals(LocalDate.of(2024, 1, 1), result.get().getDataInicio());
        assertNull(result.get().getDataFim());
        verify(membroMapper).toEntity(requestMinimo);
        verify(membroRepository).save(any(Membro.class));
    }

    @Test
    @DisplayName("Deve salvar membro com data de fim")
    void deveSalvarMembroComDataDeFim() throws Exception {
        // Given
        MembroRequestDTO requestComFim = MembroRequestDTO.builder()
                .pessoaId(1L)
                .projetoId(1L)
                .dataInicio(LocalDate.of(2024, 1, 1))
                .dataFim(LocalDate.of(2024, 6, 30))
                .build();

        Membro membroComFim = Membro.builder()
                .pessoa(pessoa)
                .projeto(projeto)
                .dataInicio(LocalDate.of(2024, 1, 1))
                .dataFim(LocalDate.of(2024, 6, 30))
                .build();

        MembroResponseDTO responseComFim = MembroResponseDTO.builder()
                .pessoa(PessoaResponseDTO.builder().id(1L).nome("João Silva").build())
                .projeto(ProjetoResponseDTO.builder().id(1L).nome("Sistema de Gestão Financeira").build())
                .dataInicio(LocalDate.of(2024, 1, 1))
                .dataFim(LocalDate.of(2024, 6, 30))
                .build();

        when(membroMapper.toEntity(requestComFim)).thenReturn(membroComFim);
        when(membroRepository.save(any(Membro.class))).thenReturn(membroComFim);
        when(membroMapper.toResponseDTO(membroComFim)).thenReturn(responseComFim);

        // When
        Optional<MembroResponseDTO> result = membroService.save(requestComFim);

        // Then
        assertNotNull(result.get());
        assertEquals(LocalDate.of(2024, 1, 1), result.get().getDataInicio());
        assertEquals(LocalDate.of(2024, 6, 30), result.get().getDataFim());
        verify(membroMapper).toEntity(requestComFim);
        verify(membroRepository).save(any(Membro.class));
    }
} 