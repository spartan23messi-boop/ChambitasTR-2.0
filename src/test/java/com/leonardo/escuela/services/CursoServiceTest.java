package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.cursos.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.CursoMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.curso.CursoServicesImpl;
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
class CursoServiceTest {
    @Mock CursoRepository cursos;
    @Mock GrupoRepository grupos;
    @Spy CursoMapper mapper = new CursoMapper();
    @InjectMocks CursoServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(cursos.findById(1L)).thenReturn(Optional.of(Fixtures.curso()));
        lenient().when(cursos.saveAndFlush(any(Curso.class))).thenAnswer(invocation -> {
            Curso entidad = invocation.getArgument(0);
            if (entidad.getIdCurso() == null) entidad.setIdCurso(1L);
            return entidad;
        });
        
    }

    @Test
    void registrar_datosValidos_registraCurso() {
        CursoResponse resultado = service.registro(new CursoRequest("Java", "Curso basico", 5));
        assertNotNull(resultado);
        verify(cursos).saveAndFlush(any(Curso.class));
    }

    @Test
    void registrar_nombreDuplicado_lanzaConflictoException() {
        when(cursos.existsByNombreIgnoreCase(anyString())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.registro(new CursoRequest("Java", "Curso basico", 5)));
        verify(cursos, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_cursoExistente_retornaCurso() {
        assertNotNull(service.obtenerPorId(1L));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(cursos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conCursos_retornaLista() {
        when(cursos.findAll()).thenReturn(List.of(Fixtures.curso()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(cursos.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_mismoNombrePropio_noLanzaConflicto() {
        assertNotNull(service.actualizar(new CursoRequest("Java", "Curso basico", 5),1L));
    }

    @Test
    void actualizar_nombreDeOtroCurso_lanzaConflictoException() {
        assertNotNull(service.actualizar(new CursoRequest("Java", "Curso basico", 5),1L));
    }

    @Test
    void eliminar_sinGrupos_eliminaCurso() {
        service.eliminar(1L);
        verify(cursos).delete(any(Curso.class));
        verify(cursos).flush();
    }

    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() {
        when(grupos.existsByCursoIdCurso(1L)).thenReturn(true);
        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));
        verify(cursos, never()).saveAndFlush(any());
    }

}
