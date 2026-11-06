package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.aula.AulaServices;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.aulas.AulaResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AulaControllerIT {
    private final AulaServices service = mock(AulaServices.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new AulaController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_registraAula() throws Exception {
        AulaResponse respuesta = new AulaResponse(3L, "Laboratorio", 30);
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/aulas").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laboratorio 1\",\"capacidad\":30}")).andExpect(status().is(201)).andExpect(jsonPath("$.idAula").value(3));
    }

    @Test
    void registrar_nombreDuplicado_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/aulas").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laboratorio 1\",\"capacidad\":30}")).andExpect(status().is(409));
    }

    @Test
    void obtenerPorId_aulaExistente_retornaAula() throws Exception {
        AulaResponse respuesta = new AulaResponse(3L, "Laboratorio", 30);
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/aulas/1")).andExpect(status().is(200)).andExpect(jsonPath("$.idAula").value(3));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/aulas/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conAulas_retornaLista() throws Exception {
        AulaResponse respuesta = new AulaResponse(3L, "Laboratorio", 30);
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/aulas")).andExpect(status().is(200)).andExpect(jsonPath("$[0].idAula").value(3));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/aulas")).andExpect(status().is(500));
    }

    @Test
    void actualizar_mismoNombrePropio_noLanzaConflicto() throws Exception {
        AulaResponse respuesta = new AulaResponse(3L, "Laboratorio", 30);
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/aulas/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laboratorio 1\",\"capacidad\":30}")).andExpect(status().is(200)).andExpect(jsonPath("$.idAula").value(3));
    }

    @Test
    void actualizar_nombreDeOtraAula_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/aulas/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laboratorio 1\",\"capacidad\":30}")).andExpect(status().is(409));
    }

    @Test
    void eliminar_sinGrupos_eliminaAula() throws Exception {
        mvc.perform(delete("/api/aulas/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() throws Exception {
        doThrow(new EntidadRelacionadaException("Entidad relacionada")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/aulas/1")).andExpect(status().is(409));
    }

}
