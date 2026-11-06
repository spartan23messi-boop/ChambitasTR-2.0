package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.aulas.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.AulaMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.aula.AulaServicesImpl;
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
class AulaServiceTest {
    @Mock AulaRepository aulas;
    @Mock GrupoRepository grupos;
    @Spy AulaMapper mapper = new AulaMapper();
    @InjectMocks AulaServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(aulas.findById(1L)).thenReturn(Optional.of(Fixtures.aula()));
        lenient().when(aulas.saveAndFlush(any(Aula.class))).thenAnswer(invocation -> {
            Aula entidad = invocation.getArgument(0);
            if (entidad.getIdAula() == null) entidad.setIdAula(1L);
            return entidad;
        });
        
    }

    @Test
    void registrar_datosValidos_registraAula() {
        AulaResponse resultado = service.registro(new AulaRequest("Laboratorio", 30));
        assertNotNull(resultado);
        verify(aulas).saveAndFlush(any(Aula.class));
    }

    @Test
    void registrar_nombreDuplicado_lanzaConflictoException() {
        when(aulas.existsByNombreIgnoreCase(anyString())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.registro(new AulaRequest("Laboratorio", 30)));
        verify(aulas, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_aulaExistente_retornaAula() {
        assertNotNull(service.obtenerPorId(1L));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(aulas.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conAulas_retornaLista() {
        when(aulas.findAll()).thenReturn(List.of(Fixtures.aula()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(aulas.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_mismoNombrePropio_noLanzaConflicto() {
        assertNotNull(service.actualizar(new AulaRequest("Laboratorio", 30),1L));
    }

    @Test
    void actualizar_nombreDeOtraAula_lanzaConflictoException() {
        when(aulas.existsByNombreIgnoreCaseAndIdAulaNot(anyString(),anyLong())).thenReturn(true);
        assertThrows(ConflictoException.class, () -> service.actualizar(new AulaRequest("Otro laboratorio",30),1L));
        verify(aulas, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_sinGrupos_eliminaAula() {
        service.eliminar(1L);
        verify(aulas).delete(any(Aula.class));
        verify(aulas).flush();
    }

    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() {
        when(grupos.existsByAulaIdAula(1L)).thenReturn(true);
        assertThrows(EntidadRelacionadaException.class, () -> service.eliminar(1L));
        verify(aulas, never()).saveAndFlush(any());
    }

}
