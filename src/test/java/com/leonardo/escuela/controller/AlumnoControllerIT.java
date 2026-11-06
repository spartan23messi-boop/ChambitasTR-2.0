package com.leonardo.escuela.controller;

import com.leonardo.escuela.services.alumno.AlumnoService;
import com.leonardo.escuela.exceptions.*;
import com.leonardo.escuela.dto.alumnos.AlumnoResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AlumnoControllerIT {
    private final AlumnoService service = mock(AlumnoService.class);
    private final MockMvc mvc = crearMvc();

    private MockMvc crearMvc() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        return MockMvcBuilders.standaloneSetup(new AlumnoController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void registrar_datosValidos_generaMatriculaYEmail() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("0.00"));
        when(service.registro(any())).thenReturn(respuesta);
        mvc.perform(post("/api/alumnos").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Carlos\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\"}")).andExpect(status().is(201)).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void registrar_nombreVacio_lanzaDatoInvalidoException() throws Exception {
        mvc.perform(post("/api/alumnos").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\"}")).andExpect(status().is(400));
        verifyNoInteractions(service);
    }

    @Test
    void obtenerPorId_alumnoExistente_retornaAlumnoConPromedio() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("0.00"));
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/alumnos/1")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.promedio").value(0.0));
    }

    @Test
    void obtenerPorId_sinCalificaciones_retornaPromedioCero() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("0.00"));
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/alumnos/1")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.promedio").value(0.0));
    }

    @Test
    void obtenerPorId_conCalificacionesNulas_ignoraNulosEnPromedio() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("8.50"));
        when(service.obtenerPorId(anyLong())).thenReturn(respuesta);
        mvc.perform(get("/api/alumnos/1")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.promedio").value(8.5));
    }

    @Test
    void obtenerPorId_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).obtenerPorId(anyLong());
        mvc.perform(get("/api/alumnos/1")).andExpect(status().is(404));
    }

    @Test
    void listar_conAlumnos_retornaLista() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("0.00"));
        when(service.Listar()).thenReturn(java.util.List.of(respuesta));
        mvc.perform(get("/api/alumnos")).andExpect(status().is(200)).andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void listar_errorDeRepositorio_propagaExcepcion() throws Exception {
        doThrow(new RuntimeException("Falló el repositorio")).when(service).Listar();
        mvc.perform(get("/api/alumnos")).andExpect(status().is(500));
    }

    @Test
    void actualizar_cambiaNombre_regeneraMatriculaYEmail() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("0.00"));
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/alumnos/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Carlos\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\"}")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_cambiaSoloApellidoMaterno_regeneraMatriculaYEmail() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("0.00"));
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/alumnos/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Carlos\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\"}")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_sinCambiosEnDatos_noRegeneraNiGuarda() throws Exception {
        AlumnoResponse respuesta = new AlumnoResponse(1L, "Carlos Garcia Lopez", "carlos@escuela.com.mx", "GALOCA2601", "2026-11-05", java.util.List.of(), new java.math.BigDecimal("0.00"));
        when(service.actualizar(any(),anyLong())).thenReturn(respuesta);
        mvc.perform(put("/api/alumnos/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Carlos\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\"}")).andExpect(status().is(200)).andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_idInexistente_lanzaRecursoNoEncontradoException() throws Exception {
        doThrow(new RecursoNoEncontradoException("No encontrado")).when(service).actualizar(any(), anyLong());
        mvc.perform(put("/api/alumnos/1").contentType(MediaType.APPLICATION_JSON).content("{\"nombre\":\"Carlos\",\"apellidoPaterno\":\"Garcia\",\"apellidoMaterno\":\"Lopez\"}")).andExpect(status().is(404));
    }

    @Test
    void eliminar_sinInscripciones_eliminaAlumno() throws Exception {
        mvc.perform(delete("/api/alumnos/1")).andExpect(status().is(204));
    }

    @Test
    void eliminar_conInscripciones_lanzaEntidadRelacionadaException() throws Exception {
        doThrow(new EntidadRelacionadaException("Entidad relacionada")).when(service).eliminar(anyLong());
        mvc.perform(delete("/api/alumnos/1")).andExpect(status().is(409));
    }

}
