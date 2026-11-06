package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.inscripciones.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class InscripcionMapperTest {
    private final InscripcionMapper mapper = new InscripcionMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        InscripcionRequest request = new InscripcionRequest(1L,1L);
        Inscripcion entidad = mapper.requestAEntidad(request,Fixtures.alumno(),Fixtures.grupo());
        assertNotNull(entidad);
        assertEquals(1L, entidad.getAlumno().getIdAlumno());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.inscripcion());
        assertNotNull(response);
        assertEquals(1L, response.idInscripcion());
    }

    @Test
    void entidadNula_noProduceResponse() {
        assertNull(mapper.entidadAResponse(null));
    }

}
