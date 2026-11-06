package com.leonardo.escuela.repositories;

import com.leonardo.escuela.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.query.parser.PartTree;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class RepositoryDerivadasTest {
    private void verificarConsultas(Class<?> repositorio, Class<?> entidad) {
        int consultas = 0;
        for (Method metodo : repositorio.getDeclaredMethods()) {
            if (!metodo.getName().startsWith("existsBy") && !metodo.getName().startsWith("findBy")) continue;
            consultas++;
            assertDoesNotThrow(() -> new PartTree(metodo.getName(), entidad),
                    "Ruta de propiedades inválida: " + metodo.getName());
        }
        assertTrue(consultas > 0, "Se esperaban consultas derivadas en " + repositorio.getSimpleName());
    }
    @Test
    void alumno_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(AlumnoRepository.class, Alumno.class);
    }

    @Test
    void maestro_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(MaestroRepository.class, Maestro.class);
    }

    @Test
    void aula_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(AulaRepository.class, Aula.class);
    }

    @Test
    void curso_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(CursoRepository.class, Curso.class);
    }

    @Test
    void grupo_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(GrupoRepository.class, Grupo.class);
    }

    @Test
    void horario_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(HorarioRepository.class, Horario.class);
    }

    @Test
    void inscripcion_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(InscripcionRepository.class, Inscripcion.class);
    }

    @Test
    void calificacion_nombresDeConsultasDerivadasSonValidos() {
        verificarConsultas(CalificacionRepository.class, Calificacion.class);
    }

}
