package com.leonardo.escuela.services;

import com.leonardo.escuela.dto.horarios.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.mapper.HorarioMapper;
import com.leonardo.escuela.repositories.*;
import com.leonardo.escuela.services.horarios.HorarioServicesImpl;
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
class HorarioServiceTest {
    @Mock HorarioRepository horarios;
    @Mock GrupoRepository grupos;
    @Spy HorarioMapper mapper = new HorarioMapper();
    @InjectMocks HorarioServicesImpl service;

    @BeforeEach
    void preparar() {
        lenient().when(horarios.findById(1L)).thenReturn(Optional.of(Fixtures.horario()));
        lenient().when(horarios.saveAndFlush(any(Horario.class))).thenAnswer(invocation -> {
            Horario entidad = invocation.getArgument(0);
            if (entidad.getIdHorario() == null) entidad.setIdHorario(1L);
            return entidad;
        });
        lenient().when(grupos.findById(1L)).thenReturn(Optional.of(Fixtures.grupo()));
        lenient().when(horarios.findByDiaAndGrupoAulaIdAulaAndGrupoPeriodo(anyString(), anyLong(), anyString())).thenReturn(List.of());
        lenient().when(horarios.findByDiaAndGrupoIdGrupo(anyString(), anyLong())).thenReturn(List.of());
    }

    @Test
    void registrar_datosValidos_registraHorario() {
        HorarioResponse resultado = service.registro(new HorarioRequest(1L, "LUNES", "08:00", "10:00"));
        assertNotNull(resultado);
        verify(horarios).saveAndFlush(any(Horario.class));
    }

    @Test
    void registrar_mismoGrupoDatosIgualesExceptoPeriodo_registraExitosamente() {
        Grupo periodoAnterior = Fixtures.grupo();
        periodoAnterior.setIdGrupo(2L);
        Horario anterior = Horario.crear(periodoAnterior, "LUNES", "08:00", "10:00");
        Grupo periodoNuevo = Fixtures.grupo();
        periodoNuevo.setPeriodo("2026-02");
        when(grupos.findById(1L)).thenReturn(Optional.of(periodoNuevo));
        when(horarios.findByDiaAndGrupoAulaIdAulaAndGrupoPeriodo("LUNES", 3L, "2026-02"))
                .thenReturn(List.of());
        HorarioResponse resultado = service.registro(new HorarioRequest(1L, "LUNES", "08:00", "10:00"));
        assertNotNull(resultado);
        assertEquals("2026-01", anterior.getGrupo().getPeriodo());
        assertEquals("2026-02", periodoNuevo.getPeriodo());
        verify(horarios).findByDiaAndGrupoAulaIdAulaAndGrupoPeriodo("LUNES", 3L, "2026-02");
        verify(horarios).saveAndFlush(any(Horario.class));
    }

    @Test
    void registrar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> service.registro(new HorarioRequest(1L, "LUNES", "10:00", "08:00")));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void registrar_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> service.registro(new HorarioRequest(1L,"LUNES","10:00","10:00")));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void registrar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() {
        Horario ocupado = Fixtures.horario();
        Grupo otroGrupo = Fixtures.grupo();
        otroGrupo.setIdGrupo(2L);
        ocupado.setGrupo(otroGrupo);
        ocupado.setIdHorario(2L);
        when(horarios.findByDiaAndGrupoAulaIdAulaAndGrupoPeriodo(anyString(),anyLong(),anyString()))
                .thenReturn(List.of(ocupado));
        assertThrows(ConflictoException.class, () -> service.registro(new HorarioRequest(1L, "LUNES", "08:00", "10:00")));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void registrar_traslapeEnElMismoGrupo_lanzaConflictoException() {
        when(horarios.findByDiaAndGrupoIdGrupo(anyString(),anyLong())).thenReturn(List.of(Fixtures.horario()));
        assertThrows(ConflictoException.class, () -> service.registro(new HorarioRequest(1L, "LUNES", "08:00", "10:00")));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void registrar_formatoHoraInvalido_retorna400() {
        assertThrows(DatoInvalidoException.class, () -> service.registro(new HorarioRequest(1L,"LUNES","8:00","10:00")));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void registrar_grupoInexistente_lanzaRecursoNoEncontradoException() {
        when(grupos.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.registro(new HorarioRequest(1L, "LUNES", "08:00", "10:00")));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void obtenerPorId_horarioExistente_retornaHorario() {
        assertNotNull(service.obtenerPorId(1L));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() {
        when(horarios.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listar_conHorarios_retornaLista() {
        when(horarios.findAll()).thenReturn(List.of(Fixtures.horario()));
        assertEquals(1, service.Listar().size());
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() {
        when(horarios.findAll()).thenThrow(new IllegalStateException("Falla de repositorio"));
        assertThrows(IllegalStateException.class, () -> service.Listar());
    }

    @Test
    void actualizar_datosValidos_actualizaHorario() {
        assertNotNull(service.actualizar(new HorarioRequest(1L,"MARTES","08:00","10:00"),1L));
        verify(horarios).saveAndFlush(any(Horario.class));
    }

    @Test
    void actualizar_mismoGrupoDatosIgualesExceptoPeriodo_actualizaExitosamente() {
        Grupo g = Fixtures.grupo(); g.setPeriodo("2026-02");
        when(grupos.findById(1L)).thenReturn(Optional.of(g));
        assertNotNull(service.actualizar(new HorarioRequest(1L,"LUNES","08:00","10:00"),1L));
        verify(horarios).saveAndFlush(any(Horario.class));
    }

    @Test
    void actualizar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> service.actualizar(new HorarioRequest(1L,"LUNES","10:00","08:00"),1L));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void actualizar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() {
        Horario ocupado = Fixtures.horario();
        Grupo otroGrupo = Fixtures.grupo();
        otroGrupo.setIdGrupo(2L);
        ocupado.setGrupo(otroGrupo);
        ocupado.setIdHorario(2L);
        when(horarios.findByDiaAndGrupoAulaIdAulaAndGrupoPeriodo(anyString(),anyLong(),anyString()))
                .thenReturn(List.of(ocupado));
        assertThrows(ConflictoException.class, () -> service.actualizar(new HorarioRequest(1L, "LUNES", "08:00", "10:00"),1L));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() {
        when(horarios.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.actualizar(new HorarioRequest(1L, "LUNES", "08:00", "10:00"),1L));
        verify(horarios, never()).saveAndFlush(any());
    }

    @Test
    void eliminar_horarioExistente_eliminaHorario() {
        service.eliminar(1L);
        verify(horarios).delete(any(Horario.class));
        verify(horarios).flush();
    }

    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() {
        when(horarios.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(1L));
        verify(horarios, never()).saveAndFlush(any());
    }

}
