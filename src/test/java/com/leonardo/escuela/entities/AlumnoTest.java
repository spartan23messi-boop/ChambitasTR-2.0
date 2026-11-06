package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AlumnoTest {

    // CREAR: DATOS VALIDOS
    @Test
    void crearAlumno_cuandoDatosSonValidos() {
        Alumno alumno = Alumno.crear("Ana", "García", "López");
        alumno.asignarDatosAcademicos("ana@escuela.com", "GALOAN2601");
        assertThat(alumno.getNombre()).isEqualTo("Ana");
        assertThat(alumno.getEmail()).isEqualTo("ana@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("GALOAN2601");
        assertThat(alumno.getFechaIngreso()).isNotNull();
        assertThat(alumno.getInscripciones()).isEmpty();
    }

    // CREAR: NOMBRE INVALIDO
    @Test
    void crearAlumno_rechazaNombreVacio() {
        assertThatThrownBy(() -> Alumno.crear("", "García", "López"))
                .isInstanceOf(DatoInvalidoException.class);
    }

    // ACTUALIZAR: DATOS VALIDOS, IDENTIFICADORES SIN CAMBIOS
    @Test
    void actualizarAlumno_modificaNombresPeroConservaDatosAcademicos() {
        Alumno alumno = Alumno.crear("Ana", "García", "López");
        alumno.asignarDatosAcademicos("ana@escuela.com", "GALOAN2601");
        alumno.actualizar("María", "Pérez", "Torres");
        assertThat(alumno.getNombre()).isEqualTo("María");
        assertThat(alumno.getApellidoPaterno()).isEqualTo("Pérez");
        assertThat(alumno.getEmail()).isEqualTo("ana@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("GALOAN2601");
    }

    // ACTUALIZAR: DATOS INVALIDOS
    @Test
    void actualizarAlumno_noModificaSiApellidoEsInvalido() {
        Alumno alumno = Alumno.crear("Ana", "García", "López");
        assertThatThrownBy(() -> alumno.actualizar("María", "  ", "Torres"))
                .isInstanceOf(DatoInvalidoException.class);
        assertThat(alumno.getNombre()).isEqualTo("Ana");
    }

    // VALIDACION: LIMPIEZA DE ESPACIOS Y CORREO
    @Test
    void asignarDatosAcademicos_limpiaEspaciosYMinusculas() {
        Alumno alumno = Alumno.crear(" Ana ", " García ", " López ");
        alumno.asignarDatosAcademicos(" ANA@ESCUELA.COM ", " GALOAN2601 ");
        assertThat(alumno.getNombre()).isEqualTo("Ana");
        assertThat(alumno.getEmail()).isEqualTo("ana@escuela.com");
        assertThat(alumno.getMatricula()).isEqualTo("GALOAN2601");
    }

    // VALIDACION: MATRICULA INVALIDA
    @Test
    void asignarDatosAcademicos_rechazaMatriculaDemasiadoLarga() {
        Alumno alumno = Alumno.crear("Ana", "García", "López");
        assertThatThrownBy(() -> alumno.asignarDatosAcademicos(
                "ana@escuela.com", "GALOAN260123"))
                .isInstanceOf(DatoInvalidoException.class);
    }
    @Test
    void calcularPromedio_sinCalificacionesDevuelveCeroConDosDecimales() {
        Alumno alumno = Alumno.crear("Ana", "García", "López");
        assertThat(alumno.calcularPromedio()).isEqualByComparingTo("0.00");
        assertThat(alumno.calcularPromedio().scale()).isEqualTo(2);
    }

    @Test
    void calcularPromedio_ignoraInscripcionesSinNota() {
        Alumno alumno = Alumno.crear("Ana", "García", "López");
        Inscripcion conNota = Inscripcion.builder().build();
        conNota.setCalificacion(Calificacion.builder().calificacion(new BigDecimal("8.0")).build());
        Inscripcion sinNota = Inscripcion.builder().build();
        alumno.setInscripciones(List.of(conNota, sinNota));
        assertThat(alumno.calcularPromedio()).isEqualByComparingTo("8.00");
    }

    @Test
    void cambiaEnDatos_noConsideraDiferentesEspaciosComoCambios() {
        Alumno alumno = Alumno.crear("Ana", "García", "López");
        assertThat(alumno.cambiaEnDatos(" Ana ", " García ", "López")).isFalse();
        assertThat(alumno.cambiaEnDatos("Ana", "García", "Pérez")).isTrue();
    }
}
