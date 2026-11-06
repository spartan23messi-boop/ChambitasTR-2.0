package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.calificaciones.CalificacionServices;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.calificaciones.CalificacionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CalificacionControllerIT {
    private final CalificacionServices service = mock(CalificacionServices.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new CalificacionController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_registraCalificacion() throws Exception {
        CalificacionResponse respuesta = new CalificacionResponse(1L,1L,new java.math.BigDecimal("8.5"),java.time.LocalDate.of(2026,11,5));
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content("{\"idInscripcion\":1,\"calificacion\":8.5}")).andExpect(status().is(201)).andExpect(jsonPath("$.idCalificacion").value(1));
    }

    @Test
    void registrar_inscripcionYaCalificada_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content("{\"idInscripcion\":1,\"calificacion\":8.5}")).andExpect(status().is(409));
    }

    @Test
    void registrar_inscripcionInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).registro(any());
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content("{\"idInscripcion\":1,\"calificacion\":8.5}")).andExpect(status().is(404));
    }

    @Test
    void registrar_calificacionNula_retorna400() throws Exception {
        mvc.perform(post("/api/calificaciones").contentType(MediaType.APPLICATION_JSON).content("{\"idInscripcion\":1,\"calificacion\":null}")).andExpect(status().is(400));
        verifyNoInteractions(service);
    }

    @Test
    void obtenerPorId_calificacionExistente_retornaCalificacion() throws Exception {
        CalificacionResponse respuesta = new CalificacionResponse(1L,1L,new java.math.BigDecimal("8.5"),java.time.LocalDate.of(2026,11,5));
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/calificaciones/1")).andExpect(status().is(200)).andExpect(jsonPath("$.idCalificacion").value(1));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/calificaciones/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conCalificaciones_retornaLista() throws Exception {
        CalificacionResponse respuesta = new CalificacionResponse(1L,1L,new java.math.BigDecimal("8.5"),java.time.LocalDate.of(2026,11,5));
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/calificaciones")).andExpect(status().is(200)).andExpect(jsonPath("$[0].idCalificacion").value(1));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/calificaciones")).andExpect(status().is(500));
    }

    @Test
    void actualizar_valorValido_actualizaCalificacion() throws Exception {
        CalificacionResponse respuesta = new CalificacionResponse(1L,1L,new java.math.BigDecimal("8.5"),java.time.LocalDate.of(2026,11,5));
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/calificaciones/1").contentType(MediaType.APPLICATION_JSON).content("{\"idInscripcion\":1,\"calificacion\":8.5}")).andExpect(status().is(200)).andExpect(jsonPath("$.idCalificacion").value(1));
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/calificaciones/1").contentType(MediaType.APPLICATION_JSON).content("{\"idInscripcion\":1,\"calificacion\":8.5}")).andExpect(status().is(404));
    }

    @Test
    void eliminar_calificacionExistente_eliminaCalificacion() throws Exception {
        mvc.perform(delete("/api/calificaciones/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/calificaciones/1")).andExpect(status().is(404));
    }

}
