package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InscripcionTest {
private final Alumno alumno = new Alumno();
    private final Grupo grupo = new Grupo();
    @Test void crear_debeRelacionarAlumnoYGrupo() {
        Inscripcion inscripcion = Inscripcion.crear(alumno, grupo);
        assertThat(inscripcion.getAlumno()).isSameAs(alumno);
        assertThat(inscripcion.getGrupo()).isSameAs(grupo);
        assertThat(inscripcion.getFechaInscripcion()).isNotNull();
    }
    @Test void crear_debeRechazarAlumnoNulo() {
        assertThatThrownBy(() -> Inscripcion.crear(null, grupo))
                .isInstanceOf(DatoInvalidoException.class);
    }
    @Test void actualizar_debeCambiarGrupoSinAlterarFecha() {
        Inscripcion inscripcion = Inscripcion.crear(alumno, grupo);
        Grupo nuevo = new Grupo();
        java.time.LocalDate fecha = inscripcion.getFechaInscripcion();
        inscripcion.actualizar(alumno, nuevo);
        assertThat(inscripcion.getGrupo()).isSameAs(nuevo);
        assertThat(inscripcion.getFechaInscripcion()).isEqualTo(fecha);
    }
    @Test void actualizar_debeConservarDatosSiGrupoNulo() {
        Inscripcion inscripcion = Inscripcion.crear(alumno, grupo);
        assertThatThrownBy(() -> inscripcion.actualizar(alumno, null))
                .isInstanceOf(DatoInvalidoException.class);
        assertThat(inscripcion.getGrupo()).isSameAs(grupo);
    }
    @Test void validarDatos_debeAceptarGrupoValido() {
        assertThat(Inscripcion.crear(alumno, grupo).getGrupo()).isNotNull();
    }
    @Test void validarDatos_debeRechazarGrupoNulo() {
        assertThatThrownBy(() -> Inscripcion.crear(alumno, null))
                .isInstanceOf(DatoInvalidoException.class);
    }
}
