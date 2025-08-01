package com.ronney.portfolio.service;

import com.ronney.portfolio.dto.request.ProjetoRequestDTO;
import com.ronney.portfolio.dto.response.ProjetoResponseDTO;
import com.ronney.portfolio.enums.RiscoProjeto;
import com.ronney.portfolio.enums.StatusProjeto;
import com.ronney.portfolio.exception.ProjetoNotFoundException;
import com.ronney.portfolio.mapper.ProjetoMapper;
import com.ronney.portfolio.model.Pessoa;
import com.ronney.portfolio.model.Projeto;
import com.ronney.portfolio.repository.ProjetoRepository;
import com.ronney.portfolio.service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ProjetoService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do ProjetoService")
class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private ProjetoMapper projetoMapper;

    @Mock
    private PessoaService pessoaService;

    @InjectMocks
    private ProjetoService projetoService;

    private Projeto projeto1;
    private Projeto projeto2;
    private ProjetoRequestDTO projetoRequestDTO;
    private ProjetoResponseDTO projetoResponseDTO;
    private Pessoa gerente;

    @BeforeEach
    void setUp() {
        gerente = Pessoa.builder()
                .id(1L)
                .nome("João Silva")
                .funcionario(true)
                .gerente(true)
                .build();

        projeto1 = Projeto.builder()
                .id(1L)
                .nome("Sistema de Gestão Financeira")
                .dataInicio(LocalDate.of(2024, 1, 15))
                .previsaoTermino(LocalDate.of(2024, 6, 30))
                .descricao("Desenvolvimento de sistema completo")
                .status(StatusProjeto.EM_ANDAMENTO)
                .orcamentoTotal(new BigDecimal("150000.00"))
                .risco(RiscoProjeto.MEDIO)
                .gerenteResponsavel(gerente)
                .build();

        projeto2 = Projeto.builder()
                .id(2L)
                .nome("Portal do Cliente")
                .dataInicio(LocalDate.of(2024, 2, 1))
                .previsaoTermino(LocalDate.of(2024, 5, 15))
                .descricao("Criação de portal web")
                .status(StatusProjeto.PLANEJADO)
                .orcamentoTotal(new BigDecimal("80000.00"))
                .risco(RiscoProjeto.BAIXO)
                .gerenteResponsavel(gerente)
                .build();

        projetoRequestDTO = ProjetoRequestDTO.builder()
                .nome("Sistema de Gestão Financeira")
                .dataInicio(LocalDate.of(2024, 1, 15))
                .previsaoTermino(LocalDate.of(2024, 6, 30))
                .descricao("Desenvolvimento de sistema completo")
                .status(StatusProjeto.EM_ANDAMENTO.name())
                .orcamentoTotal(new BigDecimal("150000.00"))
                .risco(RiscoProjeto.MEDIO.name())
                .gerenteResponsavelId(1L)
                .build();

        projetoResponseDTO = ProjetoResponseDTO.builder()
                .id(1L)
                .nome("Sistema de Gestão Financeira")
                .dataInicio(LocalDate.of(2024, 1, 15))
                .previsaoTermino(LocalDate.of(2024, 6, 30))
                .descricao("Desenvolvimento de sistema completo")
                .status(StatusProjeto.EM_ANDAMENTO)
                .orcamentoTotal(new BigDecimal("150000.00"))
                .risco(RiscoProjeto.MEDIO)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os projetos")
    void deveListarTodosOsProjetos() {
        // Given
        List<Projeto> projetos = Arrays.asList(projeto1, projeto2);
        List<ProjetoResponseDTO> expectedResponse = Arrays.asList(projetoResponseDTO, projetoResponseDTO);

        when(projetoRepository.findAll()).thenReturn(projetos);
        when(projetoMapper.toResponseDTOList(projetos)).thenReturn(expectedResponse);

        // When
        List<ProjetoResponseDTO> result = projetoService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(projetoRepository).findAll();
        verify(projetoMapper).toResponseDTOList(projetos);
    }

    @Test
    @DisplayName("Deve buscar projeto por ID")
    void deveBuscarProjetoPorId() throws Exception {
        // Given
        Long id = 1L;
        when(projetoRepository.findById(id)).thenReturn(Optional.of(projeto1));
        when(projetoMapper.toResponseDTO(projeto1)).thenReturn(projetoResponseDTO);

        // When
        Optional<ProjetoResponseDTO> result = projetoService.findById(id);

        // Then
        assertNotNull(result.get());
        assertEquals(projetoResponseDTO, result.get());
        verify(projetoRepository).findById(id);
        verify(projetoMapper).toResponseDTO(projeto1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando projeto não existe")
    void deveLancarExcecaoQuandoProjetoNaoExiste() {
        // Given
        Long id = 999L;
        when(projetoRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProjetoNotFoundException.class, () -> projetoService.findById(id));
        verify(projetoRepository).findById(id);
        verify(projetoMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve salvar um novo projeto")
    void deveSalvarUmNovoProjeto() throws Exception {
        // Given
        when(projetoMapper.toEntity(projetoRequestDTO)).thenReturn(projeto1);
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto1);
        when(projetoMapper.toResponseDTO(projeto1)).thenReturn(projetoResponseDTO);

        // When
        Optional<ProjetoResponseDTO> result = projetoService.save(projetoRequestDTO);

        // Then
        assertNotNull(result.get());
        assertEquals(projetoResponseDTO, result.get());
        verify(projetoMapper).toEntity(projetoRequestDTO);
        verify(projetoRepository).save(any(Projeto.class));
        verify(projetoMapper).toResponseDTO(projeto1);
    }

    @Test
    @DisplayName("Deve atualizar um projeto existente")
    void deveAtualizarUmProjetoExistente() throws Exception {
        // Given
        projetoRequestDTO.setId(1L);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto1));
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto1);
        when(projetoMapper.toResponseDTO(projeto1)).thenReturn(projetoResponseDTO);

        // When
        Optional<ProjetoResponseDTO> result = projetoService.update(projetoRequestDTO);

        // Then
        assertNotNull(result.get());
        assertEquals(projetoResponseDTO, result.get());
        verify(projetoRepository).findById(1L);
        verify(projetoMapper).merge(projeto1, projetoRequestDTO);
        verify(projetoRepository).save(any(Projeto.class));
        verify(projetoMapper).toResponseDTO(projeto1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar projeto inexistente")
    void deveLancarExcecaoAoAtualizarProjetoInexistente() {
        // Given
        projetoRequestDTO.setId(999L);
        when(projetoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProjetoNotFoundException.class, () -> projetoService.update(projetoRequestDTO));
        verify(projetoRepository).findById(999L);
        verify(projetoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar um projeto")
    void deveDeletarUmProjeto() throws Exception {
        // Given
        Long id = 1L;
        // Criar um projeto que pode ser excluído (status diferente de EM_ANDAMENTO)
        Projeto projetoParaExcluir = Projeto.builder()
                .id(1L)
                .nome("Projeto para Excluir")
                .status(StatusProjeto.PLANEJADO)
                .build();

        when(projetoRepository.findById(id)).thenReturn(Optional.of(projetoParaExcluir));
        doNothing().when(projetoRepository).deleteById(id);

        // When
        projetoService.delete(id);

        // Then
        verify(projetoRepository).findById(id);
        verify(projetoRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar projeto inexistente")
    void deveLancarExcecaoAoDeletarProjetoInexistente() {
        // Given
        Long id = 999L;
        when(projetoRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ProjetoNotFoundException.class, () -> projetoService.delete(id));
        verify(projetoRepository).findById(id);
        verify(projetoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há projetos")
    void deveRetornarListaVaziaQuandoNaoHaProjetos() {
        // Given
        List<Projeto> projetosVazios = Arrays.asList();
        List<ProjetoResponseDTO> responseVazio = Arrays.asList();
        when(projetoRepository.findAll()).thenReturn(projetosVazios);
        when(projetoMapper.toResponseDTOList(projetosVazios)).thenReturn(responseVazio);

        // When
        List<ProjetoResponseDTO> result = projetoService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projetoRepository).findAll();
        verify(projetoMapper).toResponseDTOList(projetosVazios);
    }

    @Test
    @DisplayName("Deve salvar projeto com dados mínimos")
    void deveSalvarProjetoComDadosMinimos() throws Exception {
        // Given
        ProjetoRequestDTO requestMinimo = ProjetoRequestDTO.builder()
                .nome("Projeto Mínimo")
                .dataInicio(LocalDate.of(2024, 1, 1))
                .status(StatusProjeto.PLANEJADO.name())
                .risco(RiscoProjeto.BAIXO.name())
                .gerenteResponsavelId(1L)
                .build();

        Projeto projetoMinimo = Projeto.builder()
                .nome("Projeto Mínimo")
                .dataInicio(LocalDate.of(2024, 1, 1))
                .status(StatusProjeto.PLANEJADO)
                .risco(RiscoProjeto.BAIXO)
                .gerenteResponsavel(gerente)
                .build();

        ProjetoResponseDTO responseMinimo = ProjetoResponseDTO.builder()
                .nome("Projeto Mínimo")
                .dataInicio(LocalDate.of(2024, 1, 1))
                .status(StatusProjeto.PLANEJADO)
                .risco(RiscoProjeto.BAIXO)
                .build();

        when(projetoMapper.toEntity(requestMinimo)).thenReturn(projetoMinimo);
        when(projetoRepository.save(any(Projeto.class))).thenReturn(projetoMinimo);
        when(projetoMapper.toResponseDTO(projetoMinimo)).thenReturn(responseMinimo);

        // When
        Optional<ProjetoResponseDTO> result = projetoService.save(requestMinimo);

        // Then
        assertNotNull(result.get());
        assertEquals("Projeto Mínimo", result.get().getNome());
        assertEquals(StatusProjeto.PLANEJADO, result.get().getStatus());
        assertEquals(RiscoProjeto.BAIXO, result.get().getRisco());
        verify(projetoMapper).toEntity(requestMinimo);
        verify(projetoRepository).save(any(Projeto.class));
    }
} 