package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.grupo.GrupoServices;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.grupos.GrupoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GrupoControllerIT {
    private final GrupoServices service = mock(GrupoServices.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new GrupoController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_registraGrupo() throws Exception {
        GrupoResponse respuesta = new GrupoResponse(1L,1L,2L,3L,"2026-01",java.util.List.of());
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/grupos").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-01\"}")).andExpect(status().is(201)).andExpect(jsonPath("$.idGrupo").value(1));
    }

    @Test
    void registrar_periodoFormatoValido_registraGrupo() throws Exception {
        GrupoResponse respuesta = new GrupoResponse(1L,1L,2L,3L,"2026-01",java.util.List.of());
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/grupos").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-01\"}")).andExpect(status().is(201)).andExpect(jsonPath("$.idGrupo").value(1));
    }

    @Test
    void registrar_combinacionDuplicada_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/grupos").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-01\"}")).andExpect(status().is(409));
    }

    @Test
    void registrar_periodoFormatoInvalido_retorna400() throws Exception {
        mvc.perform(post("/api/grupos").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-1\"}")).andExpect(status().is(400));
        verifyNoInteractions(service);
    }

    @Test
    void registrar_cursoInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).registro(any());
        mvc.perform(post("/api/grupos").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-01\"}")).andExpect(status().is(404));
    }

    @Test
    void obtenerPorId_grupoExistente_retornaGrupoConHorarios() throws Exception {
        GrupoResponse respuesta = new GrupoResponse(1L,1L,2L,3L,"2026-01",java.util.List.of());
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/grupos/1")).andExpect(status().is(200)).andExpect(jsonPath("$.idGrupo").value(1)).andExpect(jsonPath("$.horarios").isArray());
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/grupos/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conGrupos_retornaLista() throws Exception {
        GrupoResponse respuesta = new GrupoResponse(1L,1L,2L,3L,"2026-01",java.util.List.of());
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/grupos")).andExpect(status().is(200)).andExpect(jsonPath("$[0].idGrupo").value(1));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/grupos")).andExpect(status().is(500));
    }

    @Test
    void actualizar_datosValidos_actualizaGrupo() throws Exception {
        GrupoResponse respuesta = new GrupoResponse(1L,1L,2L,3L,"2026-01",java.util.List.of());
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/grupos/1").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-01\"}")).andExpect(status().is(200)).andExpect(jsonPath("$.idGrupo").value(1));
    }

    @Test
    void actualizar_combinacionDeOtroGrupo_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/grupos/1").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-01\"}")).andExpect(status().is(409));
    }

    @Test
    void actualizar_periodoFormatoInvalido_retorna400() throws Exception {
        mvc.perform(put("/api/grupos/1").contentType(MediaType.APPLICATION_JSON).content("{\"idCurso\":1,\"idMaestro\":2,\"idAula\":3,\"periodo\":\"2026-1\"}")).andExpect(status().is(400));
        verifyNoInteractions(service);
    }

    @Test
    void eliminar_sinInscripcionesNiHorarios_eliminaGrupo() throws Exception {
        mvc.perform(delete("/api/grupos/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() throws Exception {
        doThrow(new EntidadRelacionadaException("Entidad relacionada")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/grupos/1")).andExpect(status().is(409));
    }

    @Test
    void eliminar_conHorarios_lanzaEntidadRelacionadaException() throws Exception {
        doThrow(new EntidadRelacionadaException("Entidad relacionada")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/grupos/1")).andExpect(status().is(409));
    }

}
