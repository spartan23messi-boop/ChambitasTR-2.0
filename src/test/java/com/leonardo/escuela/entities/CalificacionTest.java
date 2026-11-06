package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalificacionTest {
private final Inscripcion inscripcion = new Inscripcion();
    @Test void crear_debeAsignarCalificacion() {
        Calificacion calificacion = Calificacion.crear(inscripcion, new BigDecimal("9.5"));
        assertThat(calificacion.getInscripcion()).isSameAs(inscripcion);
        assertThat(calificacion.getCalificacion()).isEqualByComparingTo("9.5");
        assertThat(calificacion.getFechaRegistro()).isNotNull();
    }
    @Test void crear_debeRechazarInscripcionNula() {
        assertThatThrownBy(() -> Calificacion.crear(null, new BigDecimal("9.0")))
                .isInstanceOf(DatoInvalidoException.class);
    }
    @Test void actualizar_debeCambiarNota() {
        Calificacion calificacion = Calificacion.crear(inscripcion, new BigDecimal("8.0"));
        calificacion.actualizar(new BigDecimal("9.5"));
        assertThat(calificacion.getCalificacion()).isEqualByComparingTo("9.5");
    }
    @Test void actualizar_debeMantenerNotaSiEsInvalida() {
        Calificacion calificacion = Calificacion.crear(inscripcion, new BigDecimal("8.0"));
        assertThatThrownBy(() -> calificacion.actualizar(new BigDecimal("100.0")))
                .isInstanceOf(DatoInvalidoException.class);
        assertThat(calificacion.getCalificacion()).isEqualByComparingTo("8.0");
    }
    @Test void validarDatos_debeAceptarCero() {
        assertThat(Calificacion.crear(inscripcion, BigDecimal.ZERO).getCalificacion())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }
    @Test void validarDatos_debeRechazarDosDecimales() {
        assertThatThrownBy(() -> Calificacion.crear(inscripcion, new BigDecimal("9.55")))
                .isInstanceOf(DatoInvalidoException.class);
    }
}
