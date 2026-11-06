package com.leonardo.escuela.mapper;

import com.leonardo.escuela.dto.horarios.*;
import com.leonardo.escuela.entities.*;
import com.leonardo.escuela.services.Fixtures;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class HorarioMapperTest {
    private final HorarioMapper mapper = new HorarioMapper();

    @Test
    void requestAEntidad_convierteDatosCorrectamente() {
        HorarioRequest request = new HorarioRequest(1L,"LUNES","08:00","10:00");
        Horario entidad = mapper.requestAEntidad(request,Fixtures.grupo());
        assertNotNull(entidad);
        assertEquals("LUNES", entidad.getDia());
    }

    @Test
    void entidadAResponse_incluyeIdentificador() {
        var response = mapper.entidadAResponse(Fixtures.horario());
        assertNotNull(response);
        assertEquals(1L, response.idHorario());
    }

    @Test
    void entidadNula_noProduceResponse() {
        assertNull(mapper.entidadAResponse(null));
    }

}
