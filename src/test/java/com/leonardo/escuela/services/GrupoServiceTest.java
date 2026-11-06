package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.grupos.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.GrupoMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.grupo.GrupoServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GrupoServiceTest {
    @Mock GrupoRepository grupos;
    @Mock CursoRepository cursos;
    @Mock MaestroRepository maestros;
    @Mock AulaRepository aulas;
    @Mock InscripcionRepository inscripciones;
    @Mock HorarioRepository horarios;
    @Spy GrupoMapper mapper = new GrupoMapper();
    @InjectMocks GrupoServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(grupos.findById(1L)).thenReturn(Optional.of(Fixtures.grupo()));
        lenient().when(grupos.saveAndFlush(any(Grupo.class))).thenAnswer(invocation -> {
            Grupo entidad = invocation.getArgument(0);
            if (entidad.getIdGrupo() == null) entidad.setIdGrupo(1L);
            return entidad;
        });
        lenient().when(cursos.findById(1L)).thenReturn(Optional.of(Fixtures.curso()));
        lenient().when(maestros.findById(2L)).thenReturn(Optional.of(Fixtures.maestro()));
        lenient().when(aulas.findById(3L)).thenReturn(Optional.of(Fixtures.aula()));
    }

    @Test
    void registrar_datosValidos_registraGrupo() {
        GrupoResponse resultado = service.registro(new GrupoRequest(1L, 2L, 3L, "2026-01"));
        assertNotNull(resultado);
        verify(grupos).saveAndFlush(any(Grupo.class));
    }

    @Test
    void registrar_periodoFormatoValido_registraGrupo() {
        GrupoResponse resultado = service.registro(new GrupoRequest(1L, 2L, 3L, "2026-01"));
        assertNotNull(resultado);
        verify(grupos).saveAndFlush(any(Grupo.class));
    }

    @Test
    void registrar_combinacionDuplicada_lanzaConflictoException() {
        when(grupos.existsByCursoIdCursoAndMaestroIdMaestroAndAulaIdAulaAndPeriodo(anyLong(),anyLong(),anyLong(),anyString())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.registro(new GrupoRequest(1L, 2L, 3L, "2026-01")));
        verify(grupos, never()).saveAndFlush(any());
    }

    @Test
    void registrar_periodoFormatoInvalido_retorna400() {
        assertThrows(DatoInvalidoException.class, () ->
                service.registro(new GrupoRequest(1L, 2L, 3L, "2026-1")));
        verify(grupos, never()).saveAndFlush(any());
    }

    @Test
    void registrar_cursoInexistente_lanzaRecursoNoEncontradoException() {
        when(cursos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.registro(new GrupoRequest(1L, 2L, 3L, "2026-01")));
        verify(grupos, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_grupoExistente_retornaGrupoConHorarios() {
        assertNotNull(service.obtenerPorId(1L).horarios());
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(grupos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conGrupos_retornaLista() {
        when(grupos.findAll()).thenReturn(List.of(Fixtures.grupo()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(grupos.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_datosValidos_actualizaGrupo() {
        assertNotNull(service.actualizar(new GrupoRequest(1L,2L,3L,"2026-02"),1L));
        verify(grupos).saveAndFlush(any(Grupo.class));
    }

    @Test
    void actualizar_combinacionDeOtroGrupo_lanzaConflictoException() {
        when(grupos.existsByCursoIdCursoAndMaestroIdMaestroAndAulaIdAulaAndPeriodoAndIdGrupoNot(anyLong(),anyLong(),anyLong(),anyString(),anyLong())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.actualizar(new GrupoRequest(1L,2L,3L,"2026-02"),1L));
        verify(grupos, never()).saveAndFlush(any());
    }

    @Test
    void actualizar_periodoFormatoInvalido_retorna400() {
        assertThrows(DatoInvalidoException.class, () ->
                service.actualizar(new GrupoRequest(1L, 2L, 3L, "2026-1"), 1L));
        verify(grupos, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_sinInscripcionesNiHorarios_eliminaGrupo() {
        service.eliminar(1L);
        verify(grupos).delete(any(Grupo.class));
        verify(grupos).flush();
    }

    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() {
        when(inscripciones.existsByGrupoIdGrupo(1L)).thenReturn(true);
        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));
        verify(grupos, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_conHorarios_lanzaEntidadRelacionadaException() {
        when(horarios.existsByGrupoIdGrupo(1L)).thenReturn(true);
        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));
        verify(grupos, never()).saveAndFlush(any());
    }

}
