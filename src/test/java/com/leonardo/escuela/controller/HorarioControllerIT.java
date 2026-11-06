package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.horarios.HorarioServices;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.horarios.HorarioResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HorarioControllerIT {
    private final HorarioServices service = mock(HorarioServices.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new HorarioController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_registraHorario() throws Exception {
        HorarioResponse respuesta = new HorarioResponse(1L,1L,"LUNES","08:00","10:00");
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(201)).andExpect(jsonPath("$.idHorario").value(1));
    }

    @Test
    void registrar_mismoGrupoDatosIgualesExceptoPeriodo_registraExitosamente() throws Exception {
        HorarioResponse respuesta = new HorarioResponse(1L,1L,"LUNES","08:00","10:00");
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(201)).andExpect(jsonPath("$.idHorario").value(1));
    }

    @Test
    void registrar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() throws Exception {
        doThrow(new DatoInvalidoException("Datos inválidos")).when(service).registro(any());
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"10:00\",\"horaFin\":\"08:00\"}")).andExpect(status().is(400));
    }

    @Test
    void registrar_horaInicioIgualAHoraFin_lanzaDatoInvalidoException() throws Exception {
        doThrow(new DatoInvalidoException("Datos inválidos")).when(service).registro(any());
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"10:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(400));
    }

    @Test
    void registrar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(409));
    }

    @Test
    void registrar_traslapeEnElMismoGrupo_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(409));
    }

    @Test
    void registrar_formatoHoraInvalido_retorna400() throws Exception {
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"8:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(400));
        verifyNoInteractions(service);
    }

    @Test
    void registrar_grupoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).registro(any());
        mvc.perform(post("/api/horarios").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(404));
    }

    @Test
    void obtenerPorId_horarioExistente_retornaHorario() throws Exception {
        HorarioResponse respuesta = new HorarioResponse(1L,1L,"LUNES","08:00","10:00");
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/horarios/1")).andExpect(status().is(200)).andExpect(jsonPath("$.idHorario").value(1));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/horarios/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conHorarios_retornaLista() throws Exception {
        HorarioResponse respuesta = new HorarioResponse(1L,1L,"LUNES","08:00","10:00");
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/horarios")).andExpect(status().is(200)).andExpect(jsonPath("$[0].idHorario").value(1));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/horarios")).andExpect(status().is(500));
    }

    @Test
    void actualizar_datosValidos_actualizaHorario() throws Exception {
        HorarioResponse respuesta = new HorarioResponse(1L,1L,"LUNES","08:00","10:00");
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/horarios/1").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(200)).andExpect(jsonPath("$.idHorario").value(1));
    }

    @Test
    void actualizar_mismoGrupoDatosIgualesExceptoPeriodo_actualizaExitosamente() throws Exception {
        HorarioResponse respuesta = new HorarioResponse(1L,1L,"LUNES","08:00","10:00");
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/horarios/1").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(200)).andExpect(jsonPath("$.idHorario").value(1));
    }

    @Test
    void actualizar_horaInicioMayorAHoraFin_lanzaDatoInvalidoException() throws Exception {
        doThrow(new DatoInvalidoException("Datos inválidos")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/horarios/1").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"10:00\",\"horaFin\":\"08:00\"}")).andExpect(status().is(400));
    }

    @Test
    void actualizar_aulaOcupadaPorOtroGrupo_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/horarios/1").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(409));
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/horarios/1").contentType(MediaType.APPLICATION_JSON).content("{\"idGrupo\":1,\"dia\":\"LUNES\",\"horaInicio\":\"08:00\",\"horaFin\":\"10:00\"}")).andExpect(status().is(404));
    }

    @Test
    void eliminar_horarioExistente_eliminaHorario() throws Exception {
        mvc.perform(delete("/api/horarios/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/horarios/1")).andExpect(status().is(404));
    }

}
