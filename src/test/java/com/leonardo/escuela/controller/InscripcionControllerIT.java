package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.inscripciones.InscripcionServices;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.inscripciones.InscripcionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InscripcionControllerIT {
    private final InscripcionServices service = mock(InscripcionServices.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new InscripcionController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_registraInscripcion() throws Exception {
        InscripcionResponse respuesta = new InscripcionResponse(1L,1L,1L,java.time.LocalDate.of(2026,11,5),null);
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/inscripciones").contentType(MediaType.APPLICATION_JSON).content("{\"idAlumno\":1,\"idGrupo\":2}")).andExpect(status().is(201)).andExpect(jsonPath("$.idInscripcion").value(1));
    }

    @Test
    void registrar_alumnoYGrupoDuplicados_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/inscripciones").contentType(MediaType.APPLICATION_JSON).content("{\"idAlumno\":1,\"idGrupo\":2}")).andExpect(status().is(409));
    }

    @Test
    void registrar_alumnoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).registro(any());
        mvc.perform(post("/api/inscripciones").contentType(MediaType.APPLICATION_JSON).content("{\"idAlumno\":1,\"idGrupo\":2}")).andExpect(status().is(404));
    }

    @Test
    void obtenerPorId_inscripcionExistente_retornaInscripcion() throws Exception {
        InscripcionResponse respuesta = new InscripcionResponse(1L,1L,1L,java.time.LocalDate.of(2026,11,5),null);
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/inscripciones/1")).andExpect(status().is(200)).andExpect(jsonPath("$.idInscripcion").value(1));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/inscripciones/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conInscripciones_retornaLista() throws Exception {
        InscripcionResponse respuesta = new InscripcionResponse(1L,1L,1L,java.time.LocalDate.of(2026,11,5),null);
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/inscripciones")).andExpect(status().is(200)).andExpect(jsonPath("$[0].idInscripcion").value(1));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/inscripciones")).andExpect(status().is(500));
    }

    @Test
    void actualizar_otroGrupoValido_actualizaInscripcion() throws Exception {
        InscripcionResponse respuesta = new InscripcionResponse(1L,1L,1L,java.time.LocalDate.of(2026,11,5),null);
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/inscripciones/1").contentType(MediaType.APPLICATION_JSON).content("{\"idAlumno\":1,\"idGrupo\":2}")).andExpect(status().is(200)).andExpect(jsonPath("$.idInscripcion").value(1));
    }

    @Test
    void actualizar_combinacionYaExistente_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/inscripciones/1").contentType(MediaType.APPLICATION_JSON).content("{\"idAlumno\":1,\"idGrupo\":2}")).andExpect(status().is(409));
    }

    @Test
    void eliminar_sinCalificacion_eliminaInscripcion() throws Exception {
        mvc.perform(delete("/api/inscripciones/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_conCalificacion_lanzaEntidadRelacionadaException() throws Exception {
        doThrow(new EntidadRelacionadaException("Entidad relacionada")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/inscripciones/1")).andExpect(status().is(409));
    }

}
