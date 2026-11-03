package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HorarioTest {
private final Grupo grupo = new Grupo();
    @Test void crear_debeAsignarHorarioValido() {
        Horario horario = Horario.crear(grupo, "lunes", "08:00", "09:30");
        assertThat(horario.getGrupo()).isSameAs(grupo);
        assertThat(horario.getDia()).isEqualTo("LUNES");
        assertThat(horario.getHoraInicio()).isEqualTo("08:00");
    }
    @Test void crear_debeRechazarGrupoNulo() {
        assertThatThrownBy(() -> Horario.crear(null, "LUNES", "08:00", "09:00"))
                .isInstanceOf(DatoInvalidoException.class);
    }
    @Test void actualizar_debeCambiarHorario() {
        Horario horario = Horario.crear(grupo, "LUNES", "08:00", "09:00");
        horario.actualizar(grupo, "miércoles", "10:00", "11:30");
        assertThat(horario.getDia()).isEqualTo("MIERCOLES");
        assertThat(horario.getHoraInicio()).isEqualTo("10:00");
        assertThat(horario.getHoraFin()).isEqualTo("11:30");
    }
    @Test void actualizar_debeConservarDatosSiIntervaloInvalido() {
        Horario horario = Horario.crear(grupo, "LUNES", "08:00", "09:00");
        assertThatThrownBy(() -> horario.actualizar(grupo, "MARTES", "12:00", "11:00"))
                .isInstanceOf(DatoInvalidoException.class);
        assertThat(horario.getDia()).isEqualTo("LUNES");
        assertThat(horario.getHoraInicio()).isEqualTo("08:00");
    }
    @Test void validarDatos_debeAceptarHoraCorrecta() {
        assertThat(Horario.crear(grupo, "VIERNES", "00:00", "23:59").getHoraFin())
                .isEqualTo("23:59");
    }
    @Test void validarDatos_debeRechazarHoraInvalida() {
        assertThatThrownBy(() -> Horario.crear(grupo, "LUNES", "25:00", "26:00"))
                .isInstanceOf(DatoInvalidoException.class);
    }
}
