package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.maestro.MaestroService;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.maestros.MaestroResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MaestroControllerIT {
    private final MaestroService service = mock(MaestroService.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new MaestroController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_registraMaestro() throws Exception {
        MaestroResponse respuesta = new MaestroResponse(2L, "Laura Garcia Lopez", "laura@test.com", "2223456789", java.util.List.of());
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/maestros").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laura\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\",\"email\":\"laura@test.com\",\"telefono\":\"2223456789\"}")).andExpect(status().is(201)).andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void registrar_emailDuplicado_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/maestros").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laura\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\",\"email\":\"laura@test.com\",\"telefono\":\"2223456789\"}")).andExpect(status().is(409));
    }

    @Test
    void registrar_telefonoDuplicado_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).registro(any());
        mvc.perform(post("/api/maestros").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laura\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\",\"email\":\"laura@test.com\",\"telefono\":\"2223456789\"}")).andExpect(status().is(409));
    }

    @Test
    void obtenerPorId_maestroExistente_retornaMaestroConCursos() throws Exception {
        MaestroResponse respuesta = new MaestroResponse(2L, "Laura Garcia Lopez", "laura@test.com", "2223456789", java.util.List.of());
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/maestros/1")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/maestros/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conMaestros_retornaLista() throws Exception {
        MaestroResponse respuesta = new MaestroResponse(2L, "Laura Garcia Lopez", "laura@test.com", "2223456789", java.util.List.of());
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/maestros")).andExpect(status().is(200)).andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/maestros")).andExpect(status().is(500));
    }

    @Test
    void actualizar_mismoEmailYTelefonoPropios_noLanzaConflicto() throws Exception {
        MaestroResponse respuesta = new MaestroResponse(2L, "Laura Garcia Lopez", "laura@test.com", "2223456789", java.util.List.of());
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/maestros/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laura\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\",\"email\":\"laura@test.com\",\"telefono\":\"2223456789\"}")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void actualizar_emailDeOtroMaestro_lanzaConflictoException() throws Exception {
        doThrow(new ConflictoException("Registro duplicado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/maestros/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Laura\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\",\"email\":\"laura@test.com\",\"telefono\":\"2223456789\"}")).andExpect(status().is(409));
    }

    @Test
    void eliminar_sinGrupos_eliminaMaestro() throws Exception {
        mvc.perform(delete("/api/maestros/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_conGrupos_lanzaEntidadRelacionadaException() throws Exception {
        doThrow(new EntidadRelacionadaException("Entidad relacionada")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/maestros/1")).andExpect(status().is(409));
    }

}
