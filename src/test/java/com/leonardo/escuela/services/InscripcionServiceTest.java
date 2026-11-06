package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.inscripciones.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.InscripcionMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.inscripciones.InscripcionServicesImpl;
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
class InscripcionServiceTest {
    @Mock InscripcionRepository inscripciones;
    @Mock AlumnoRepository alumnos;
    @Mock GrupoRepository grupos;
    @Mock CalificacionRepository calificaciones;
    @Spy InscripcionMapper mapper = new InscripcionMapper();
    @InjectMocks InscripcionServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(inscripciones.findById(1L)).thenReturn(Optional.of(Fixtures.inscripcion()));
        lenient().when(inscripciones.saveAndFlush(any(Inscripcion.class))).thenAnswer(invocation -> {
            Inscripcion entidad = invocation.getArgument(0);
            if (entidad.getIdInscripcion() == null) entidad.setIdInscripcion(1L);
            return entidad;
        });
        lenient().when(alumnos.findById(1L)).thenReturn(Optional.of(Fixtures.alumno()));
        lenient().when(grupos.findById(1L)).thenReturn(Optional.of(Fixtures.grupo()));
    }

    @Test
    void registrar_datosValidos_registraInscripcion() {
        InscripcionResponse resultado = service.registro(new InscripcionRequest(1L, 1L));
        assertNotNull(resultado);
        verify(inscripciones).saveAndFlush(any(Inscripcion.class));
        assertNull(resultado.calificacion());
    }

    @Test
    void registrar_alumnoYGrupoDuplicados_lanzaConflictoException() {
        when(inscripciones.existsByAlumnoIdAlumnoAndGrupoIdGrupo(anyLong(),anyLong())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.registro(new InscripcionRequest(1L, 1L)));
        verify(inscripciones, never()).saveAndFlush(any());
    }

    @Test
    void registrar_alumnoInexistente_lanzaRecursoNoEncontradoException() {
        when(alumnos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.registro(new InscripcionRequest(1L, 1L)));
        verify(inscripciones, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_inscripcionExistente_retornaInscripcion() {
        assertNotNull(service.obtenerPorId(1L));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(inscripciones.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conInscripciones_retornaLista() {
        when(inscripciones.findAll()).thenReturn(List.of(Fixtures.inscripcion()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(inscripciones.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_otroGrupoValido_actualizaInscripcion() {
        Grupo otro=Fixtures.grupo(); otro.setIdGrupo(2L);
        when(grupos.findById(2L)).thenReturn(Optional.of(otro));
        assertNotNull(service.actualizar(new InscripcionRequest(1L,2L),1L));
        verify(inscripciones).saveAndFlush(any(Inscripcion.class));
    }

    @Test
    void actualizar_combinacionYaExistente_lanzaConflictoException() {
        when(inscripciones.existsByAlumnoIdAlumnoAndGrupoIdGrupoAndIdInscripcionNot(anyLong(),anyLong(),anyLong())).thenReturn(true);
        Grupo nuevo=Fixtures.grupo(); nuevo.setIdGrupo(2L);
        when(grupos.findById(2L)).thenReturn(Optional.of(nuevo));
        assertThrows(ConflictoException.class, () -> service.actualizar(new InscripcionRequest(1L,2L),1L));
        verify(inscripciones, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_sinCalificacion_eliminaInscripcion() {
        service.eliminar(1L);
        verify(inscripciones).delete(any(Inscripcion.class));
        verify(inscripciones).flush();
    }

    @Test
    void eliminar_conCalificacion_lanzaEntidadRelacionadaException() {
        when(calificaciones.existsByInscripcionIdInscripcion(1L)).thenReturn(true);
        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));
        verify(inscripciones, never()).saveAndFlush(any());
    }

}
