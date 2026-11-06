package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.maestros.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.MaestroMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.maestro.MaestroServicesImpl;
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
class MaestroServiceTest {
    @Mock MaestroRepository maestros;
    @Mock GrupoRepository grupos;
    @Spy MaestroMapper mapper = new MaestroMapper();
    @InjectMocks MaestroServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(maestros.findById(1L)).thenReturn(Optional.of(Fixtures.maestro()));
        lenient().when(maestros.saveAndFlush(any(Maestro.class))).thenAnswer(invocation -> {
            Maestro entidad = invocation.getArgument(0);
            if (entidad.getIdMaestro() == null) entidad.setIdMaestro(1L);
            return entidad;
        });
        
    }

    @Test
    void registrar_datosValidos_registraMaestro() {
        MaestroResponse resultado = service.registro(new MaestroRequest("Laura", "Garcia", "Lopez", "laura@test.com", "2223456789"));
        assertNotNull(resultado);
        verify(maestros).saveAndFlush(any(Maestro.class));
    }

    @Test
    void registrar_emailDuplicado_lanzaConflictoException() {
        when(maestros.existsByEmailIgnoreCase(anyString())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.registro(new MaestroRequest("Laura", "Garcia", "Lopez", "laura@test.com", "2223456789")));
        verify(maestros, never()).saveAndFlush(any());
    }

    @Test
    void registrar_telefonoDuplicado_lanzaConflictoException() {
        when(maestros.existsByTelefono(anyString())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.registro(new MaestroRequest("Laura", "Garcia", "Lopez", "laura@test.com", "2223456789")));
        verify(maestros, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_maestroExistente_retornaMaestroConCursos() {
        assertNotNull(service.obtenerPorId(1L).cursos());
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(maestros.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conMaestros_retornaLista() {
        when(maestros.findAll()).thenReturn(List.of(Fixtures.maestro()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(maestros.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_mismoEmailYTelefonoPropios_noLanzaConflicto() {
        assertNotNull(service.actualizar(new MaestroRequest("Laura", "Garcia", "Lopez", "laura@test.com", "2223456789"),1L));
        verify(maestros).saveAndFlush(any(Maestro.class));
    }

    @Test
    void actualizar_emailDeOtroMaestro_lanzaConflictoException() {
        when(maestros.existsByEmailIgnoreCaseAndIdMaestroNot(anyString(),anyLong())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.actualizar(new MaestroRequest("Laura", "Garcia", "Lopez", "laura@test.com", "2223456789"),1L));
        verify(maestros, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_sinGrupos_eliminaMaestro() {
        service.eliminar(1L);
        verify(maestros).delete(any(Maestro.class));
        verify(maestros).flush();
    }

    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() {
        when(grupos.existsByMaestroIdMaestro(1L)).thenReturn(true);
        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));
        verify(maestros, never()).saveAndFlush(any());
    }

}
