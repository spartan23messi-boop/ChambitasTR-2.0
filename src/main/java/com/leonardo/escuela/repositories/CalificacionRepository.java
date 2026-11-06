package com.leonardo.escuela.repositories;
import com.leonardo.escuela.entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CalificacionRepository extends JpaRepository<Calificacion,Long> {
    boolean existsByInscripcionIdInscripcion(Long idInscripcion);
}
