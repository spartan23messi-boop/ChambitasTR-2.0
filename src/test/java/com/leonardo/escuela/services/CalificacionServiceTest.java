package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.calificaciones.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.CalificacionMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.calificaciones.CalificacionServicesImpl;
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
class CalificacionServiceTest {
    @Mock CalificacionRepository calificaciones;
    @Mock InscripcionRepository inscripciones;
    @Spy CalificacionMapper mapper = new CalificacionMapper();
    @InjectMocks CalificacionServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(calificaciones.findById(1L)).thenReturn(Optional.of(Fixtures.calificacion()));
        lenient().when(calificaciones.saveAndFlush(any(Calificacion.class))).thenAnswer(invocation -> {
            Calificacion entidad = invocation.getArgument(0);
            if (entidad.getIdCalificacion() == null) entidad.setIdCalificacion(1L);
            return entidad;
        });
        lenient().when(inscripciones.findById(1L)).thenReturn(Optional.of(Fixtures.inscripcion()));
    }

    @Test
    void registrar_datosValidos_registraCalificacion() {
        CalificacionResponse resultado = service.registro(new CalificacionRequest(1L, new BigDecimal("8.5")));
        assertNotNull(resultado);
        verify(calificaciones).saveAndFlush(any(Calificacion.class));
    }

    @Test
    void registrar_inscripcionYaCalificada_lanzaConflictoException() {
        when(calificaciones.existsByInscripcionIdInscripcion(1L)).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.registro(new CalificacionRequest(1L, new BigDecimal("8.5"))));
        verify(calificaciones, never()).saveAndFlush(any());
    }

    @Test
    void registrar_inscripcionInexistente_lanzaRecursoNoEncontradoException() {
        when(inscripciones.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.registro(new CalificacionRequest(1L, new BigDecimal("8.5"))));
        verify(calificaciones, never()).saveAndFlush(any());
    }

    @Test
    void registrar_calificacionNula_retorna400() {
        assertThrows(DatoInvalidoException.class, () -> service.registro(new CalificacionRequest(1L,null)));
        verify(calificaciones, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_calificacionExistente_retornaCalificacion() {
        assertNotNull(service.obtenerPorId(1L));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(calificaciones.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conCalificaciones_retornaLista() {
        when(calificaciones.findAll()).thenReturn(List.of(Fixtures.calificacion()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(calificaciones.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_valorValido_actualizaCalificacion() {
        assertNotNull(service.actualizar(new CalificacionRequest(1L,new BigDecimal("9.2")),1L));
        verify(calificaciones).saveAndFlush(any(Calificacion.class));
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        when(calificaciones.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(new CalificacionRequest(1L, new BigDecimal("8.5")),1L));
        verify(calificaciones, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_calificacionExistente_eliminaCalificacion() {
        service.eliminar(1L);
        verify(calificaciones).delete(any(Calificacion.class));
        verify(calificaciones).flush();
    }

    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() {
        when(calificaciones.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(1L));
        verify(calificaciones, never()).saveAndFlush(any());
    }

}
