package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.calificaciones.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CalificacionMapperTest {
    private final CalificacionMapper mapper = new CalificacionMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        CalificacionRequest request = new CalificacionRequest(1L,new BigDecimal("8.5"));
        Calificacion entidad = mapper.requestAEntidad(request,Fixtures.inscripcion());
        assertNotNull(entidad);
        assertEquals(new BigDecimal("8.5"), entidad.getCalificacion());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.calificacion());
        assertNotNull(response);
        assertEquals(1L, response.idCalificacion());
    }

    @Test
    void entidadNula_noProduceResponse() {
        assertNull(mapper.entidadAResponse(null));
    }

}
