package com.ronney.portfolio.service;

import com.ronney.portfolio.dto.request.PessoaRequestDTO;
import com.ronney.portfolio.dto.response.PessoaResponseDTO;
import com.ronney.portfolio.exception.PessoaNotFoundException;
import com.ronney.portfolio.mapper.PessoaMapper;
import com.ronney.portfolio.model.Pessoa;
import com.ronney.portfolio.repository.PessoaRepository;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do PessoaService")
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private PessoaMapper pessoaMapper;

    @InjectMocks
    private PessoaService pessoaService;

    private Pessoa pessoa1;
    private Pessoa pessoa2;
    private PessoaRequestDTO pessoaRequestDTO;
    private PessoaResponseDTO pessoaResponseDTO;

    @BeforeEach
    void setUp() {
        pessoa1 = Pessoa.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("123.456.789-00")
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .funcionario(true)
                .gerente(true)
                .build();

        pessoa2 = Pessoa.builder()
                .id(2L)
                .nome("Maria Santos")
                .cpf("987.654.321-00")
                .dataNascimento(LocalDate.of(1990, 8, 22))
                .funcionario(true)
                .gerente(true)
                .build();

        pessoaRequestDTO = PessoaRequestDTO.builder()
                .nome("João Silva")
                .cpf("123.456.789-00")
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .funcionario(true)
                .gerente(true)
                .build();

        pessoaResponseDTO = PessoaResponseDTO.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("123.456.789-00")
                .dataNascimento(LocalDate.of(1985, 5, 15))
                .funcionario(true)
                .gerente(true)
                .build();
    }

    @Test
    @DisplayName("Deve listar todas as pessoas")
    void deveListarTodasAsPessoas() {
        // Given
        List<Pessoa> pessoas = Arrays.asList(pessoa1, pessoa2);
        List<PessoaResponseDTO> expectedResponse = Arrays.asList(pessoaResponseDTO, pessoaResponseDTO);

        when(pessoaRepository.findAll()).thenReturn(pessoas);
        when(pessoaMapper.toResponseDTOList(pessoas)).thenReturn(expectedResponse);

        // When
        List<PessoaResponseDTO> result = pessoaService.findAll();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pessoaRepository).findAll();
        verify(pessoaMapper).toResponseDTOList(pessoas);
    }

    @Test
    @DisplayName("Deve buscar pessoa por ID")
    void deveBuscarPessoaPorId() throws Exception {
        // Given
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa1));
        when(pessoaMapper.toResponseDTO(pessoa1)).thenReturn(pessoaResponseDTO);

        // When
        Optional<PessoaResponseDTO> result = pessoaService.findById(id);

        // Then
        assertNotNull(result.get());
        assertEquals(pessoaResponseDTO, result.get());
        verify(pessoaRepository).findById(id);
        verify(pessoaMapper).toResponseDTO(pessoa1);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pessoa não existe")
    void deveLancarExcecaoQuandoPessoaNaoExiste() {
        // Given
        Long id = 999L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PessoaNotFoundException.class, () -> pessoaService.findById(id));
        verify(pessoaRepository).findById(id);
        verify(pessoaMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve buscar pessoa por ID como entidade")
    void deveBuscarPessoaPorIdComoEntidade() {
        // Given
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa1));

        // When
        Optional<Pessoa> result = pessoaService.findByIdEntity(id);

        // Then
        assertNotNull(result);
        assertEquals(pessoa1, result.get());
        assertEquals("João Silva", result.get().getNome());
        verify(pessoaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pessoa não existe (entidade)")
    void deveLancarExcecaoQuandoPessoaNaoExisteEntidade() {
        // Given
        Long id = 999L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        Optional<Pessoa> result = pessoaService.findByIdEntity(id);
        assertTrue(result.isEmpty());
        verify(pessoaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve salvar uma nova pessoa")
    void deveSalvarUmaNovaPessoa() throws Exception {
        // Given
        when(pessoaMapper.toEntity(pessoaRequestDTO)).thenReturn(pessoa1);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa1);
        when(pessoaMapper.toResponseDTO(pessoa1)).thenReturn(pessoaResponseDTO);

        // When
        Optional<PessoaResponseDTO> result = pessoaService.save(pessoaRequestDTO);

        // Then
        assertNotNull(result.get());
        assertEquals(pessoaResponseDTO, result.get());
        verify(pessoaMapper).toEntity(pessoaRequestDTO);
        verify(pessoaRepository).save(any(Pessoa.class));
        verify(pessoaMapper).toResponseDTO(pessoa1);
    }

    @Test
    @DisplayName("Deve atualizar uma pessoa existente")
    void deveAtualizarUmaPessoaExistente() throws Exception {
        // Given
        pessoaRequestDTO.setId(1L);
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa1));
        when(pessoaMapper.toEntity(pessoaRequestDTO)).thenReturn(pessoa1);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa1);
        when(pessoaMapper.toResponseDTO(pessoa1)).thenReturn(pessoaResponseDTO);

        // When
        Optional<PessoaResponseDTO> result = pessoaService.update(pessoaRequestDTO);

        // Then
        assertNotNull(result);
        assertEquals(pessoaResponseDTO, result.get());
        verify(pessoaRepository).findById(1L);
        verify(pessoaMapper).toEntity(pessoaRequestDTO);
        verify(pessoaRepository).save(any(Pessoa.class));
        verify(pessoaMapper).toResponseDTO(pessoa1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar pessoa inexistente")
    void deveLancarExcecaoAoAtualizarPessoaInexistente() {
        // Given
        pessoaRequestDTO.setId(999L);
        when(pessoaRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PessoaNotFoundException.class, () -> pessoaService.update(pessoaRequestDTO));
        verify(pessoaRepository).findById(999L);
        verify(pessoaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar uma pessoa")
    void deveDeletarUmaPessoa() throws Exception {
        // Given
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa1));
        doNothing().when(pessoaRepository).deleteById(id);

        // When
        pessoaService.delete(id);

        // Then
        verify(pessoaRepository).findById(id);
        verify(pessoaRepository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar pessoa inexistente")
    void deveLancarExcecaoAoDeletarPessoaInexistente() {
        // Given
        Long id = 999L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PessoaNotFoundException.class, () -> pessoaService.delete(id));
        verify(pessoaRepository).findById(id);
        verify(pessoaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve buscar pessoa por ID (entidade) com sucesso")
    void deveBuscarPessoaPorIdEntidadeComSucesso() {
        // Given
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa1));

        // When
        Optional<Pessoa> result = pessoaService.findByIdEntity(id);

        // Then
        assertNotNull(result);
        assertEquals(pessoa1, result.get());
        assertEquals("João Silva", result.get().getNome());
        verify(pessoaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há pessoas")
    void deveRetornarListaVaziaQuandoNaoHaPessoas() {
        // Given
        List<Pessoa> pessoasVazias = Arrays.asList();
        List<PessoaResponseDTO> responseVazio = Arrays.asList();
        when(pessoaRepository.findAll()).thenReturn(pessoasVazias);
        when(pessoaMapper.toResponseDTOList(pessoasVazias)).thenReturn(responseVazio);

        // When
        List<PessoaResponseDTO> result = pessoaService.findAll();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pessoaRepository).findAll();
        verify(pessoaMapper).toResponseDTOList(pessoasVazias);
    }

    @Test
    @DisplayName("Deve salvar pessoa com dados mínimos")
    void deveSalvarPessoaComDadosMinimos() throws Exception {
        // Given
        PessoaRequestDTO requestMinimo = PessoaRequestDTO.builder()
                .nome("Pessoa Mínima")
                .funcionario(false)
                .gerente(false)
                .build();

        Pessoa pessoaMinima = Pessoa.builder()
                .nome("Pessoa Mínima")
                .funcionario(false)
                .gerente(false)
                .build();

        PessoaResponseDTO responseMinimo = PessoaResponseDTO.builder()
                .nome("Pessoa Mínima")
                .funcionario(false)
                .gerente(false)
                .build();

        when(pessoaMapper.toEntity(requestMinimo)).thenReturn(pessoaMinima);
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaMinima);
        when(pessoaMapper.toResponseDTO(pessoaMinima)).thenReturn(responseMinimo);

        // When
        Optional<PessoaResponseDTO> result = pessoaService.save(requestMinimo);

        // Then
        assertNotNull(result.get());
        assertEquals("Pessoa Mínima", result.get().getNome());
        assertFalse(result.get().getFuncionario());
        assertFalse(result.get().getGerente());
        verify(pessoaMapper).toEntity(requestMinimo);
        verify(pessoaRepository).save(any(Pessoa.class));
    }
} 