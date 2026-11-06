package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.alumnos.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.AlumnoMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.alumno.AlumnoServicesImpl;
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
class AlumnoServiceTest {
    @Mock AlumnoRepository alumnos;
    @Mock InscripcionRepository inscripciones;
    @Spy AlumnoMapper mapper = new AlumnoMapper();
    @InjectMocks AlumnoServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(alumnos.findById(1L)).thenReturn(Optional.of(Fixtures.alumno()));
        lenient().when(alumnos.saveAndFlush(any(Alumno.class))).thenAnswer(invocation -> {
            Alumno entidad = invocation.getArgument(0);
            if (entidad.getIdAlumno() == null) entidad.setIdAlumno(1L);
            return entidad;
        });
        lenient().when(alumnos.generarMatricula(anyString(), anyString(), anyString())).thenReturn("GALOCA2601");
        lenient().when(alumnos.generarEmail(anyString(), anyString(), anyString())).thenReturn("carlos@escuela.com.mx");
    }

    @Test
    void registrar_datosValidos_generaMatriculaYEmail() {
        AlumnoResponse resultado = service.registro(new AlumnoRequest("Carlos", "Garcia", "Lopez"));
        assertNotNull(resultado);
        verify(alumnos).saveAndFlush(any(Alumno.class));
        assertEquals("GALOCA2601", resultado.matricula());
        assertNotNull(resultado.email());
    }

    @Test
    void registrar_nombreVacio_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> service.registro(new AlumnoRequest("", "Garcia", "Lopez")));
        verify(alumnos, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_alumnoExistente_retornaAlumnoConPromedio() {
        Alumno a = Fixtures.alumno();
        Inscripcion primera = Fixtures.inscripcion();
        primera.setCalificacion(Calificacion.crear(primera, new BigDecimal("9.0")));
        Inscripcion segunda = Fixtures.inscripcion();
        segunda.setCalificacion(Calificacion.crear(segunda, new BigDecimal("8.0")));
        a.setInscripciones(List.of(primera, segunda));
        when(alumnos.findById(1L)).thenReturn(Optional.of(a));
        assertEquals(new BigDecimal("8.50"), service.obtenerPorId(1L).promedio());
    }

    @Test
    void obtenerPorId_sinCalificaciones_retornaPromedioCero() {
        Alumno a = Fixtures.alumno();
        when(alumnos.findById(1L)).thenReturn(Optional.of(a));
        assertEquals(new BigDecimal("0.00"), service.obtenerPorId(1L).promedio());
    }

    @Test
    void obtenerPorId_conCalificacionesNulas_ignoraNulosEnPromedio() {
        Alumno a = Fixtures.alumno();
        Inscripcion sinNota = Fixtures.inscripcion();
        sinNota.setCalificacion(null);
        Inscripcion conNota = Fixtures.inscripcion();
        conNota.setCalificacion(Calificacion.crear(conNota, new BigDecimal("8.5")));
        a.setInscripciones(List.of(sinNota, conNota));
        when(alumnos.findById(1L)).thenReturn(Optional.of(a));
        assertEquals(new BigDecimal("8.50"), service.obtenerPorId(1L).promedio());
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(alumnos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conAlumnos_retornaLista() {
        when(alumnos.findAll()).thenReturn(List.of(Fixtures.alumno()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(alumnos.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_cambiaNombre_regeneraMatriculaYEmail() {
        assertNotNull(service.actualizar(new AlumnoRequest("Juan","Garcia","Lopez"),1L));
        verify(alumnos).generarMatricula(anyString(),anyString(),anyString());
        verify(alumnos).generarEmail(anyString(),anyString(),anyString());
        verify(alumnos).saveAndFlush(any(Alumno.class));
    }

    @Test
    void actualizar_cambiaSoloApellidoMaterno_regeneraMatriculaYEmail() {
        assertNotNull(service.actualizar(new AlumnoRequest("Carlos","Garcia","Perez"),1L));
        verify(alumnos).generarMatricula(anyString(),anyString(),anyString());
        verify(alumnos).generarEmail(anyString(),anyString(),anyString());
        verify(alumnos).saveAndFlush(any(Alumno.class));
    }

    @Test
    void actualizar_sinCambiosEnDatos_noRegeneraNiGuarda() {
        assertNotNull(service.actualizar(new AlumnoRequest("Carlos", "Garcia", "Lopez"),1L));
        verify(alumnos,never()).generarMatricula(anyString(),anyString(),anyString());
        verify(alumnos,never()).saveAndFlush(any());
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        when(alumnos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(new AlumnoRequest("Carlos", "Garcia", "Lopez"),1L));
        verify(alumnos, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_sinInscripciones_eliminaAlumno() {
        service.eliminar(1L);
        verify(alumnos).delete(any(Alumno.class));
        verify(alumnos).flush();
    }

    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() {
        when(inscripciones.existsByAlumnoIdAlumno(1L)).thenReturn(true);
        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));
        verify(alumnos, never()).saveAndFlush(any());
    }

}
