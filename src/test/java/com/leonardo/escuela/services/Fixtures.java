package com.leonardo.escuela.services;

import com.leonardo.escuela.entities.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public final class Fixtures {
    private Fixtures() {}
    public static Alumno alumno() {
        Alumno a = Alumno.crear("Carlos", "Garcia", "Lopez");
        a.setIdAlumno(1L);
        a.asignarDatosAcademicos("carlos@escuela.com.mx", "GALOCA2601");
        return a;
    }
    public static Maestro maestro() {
        Maestro m = Maestro.crear("Laura", "Garcia", "Lopez", "laura@test.com", "2223456789");
        m.setIdMaestro(2L); return m;
    }
    public static Aula aula() {
        Aula a = Aula.crear("Laboratorio", 30); a.setIdAula(3L); return a;
    }
    public static Curso curso() {
        Curso c = Curso.crear("Java", "Curso basico", 5); c.setIdCurso(1L); return c;
    }
    public static Grupo grupo() {
        Grupo g = Grupo.crear(curso(), maestro(), aula(), "2026-01");
        g.setIdGrupo(1L); return g;
    }
    public static Horario horario() {
        Horario h = Horario.crear(grupo(), "LUNES", "08:00", "10:00");
        h.setIdHorario(1L); return h;
    }
    public static Inscripcion inscripcion() {
        Inscripcion i = Inscripcion.crear(alumno(), grupo());
        i.setIdInscripcion(1L); return i;
    }
    public static Calificacion calificacion() {
        Calificacion c = Calificacion.crear(inscripcion(), new BigDecimal("8.5"));
        c.setIdCalificacion(1L); return c;
    }
}
