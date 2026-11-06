package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.alumnos.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class AlumnoMapperTest {
    private final AlumnoMapper mapper = new AlumnoMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        AlumnoRequest request = new AlumnoRequest("Carlos","Garcia","Lopez");
        Alumno entidad = mapper.requestAEntidad(request);
        assertNotNull(entidad);
        assertEquals("Carlos", entidad.getNombre());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.alumno());
        assertNotNull(response);
        assertEquals(1L, response.id());
    }

    @Test
    void parametrosNulos_noProducenRespuesta() {
        assertNull(mapper.requestAEntidad(null));
        assertNull(mapper.entidadAResponse(null));
    }

}
