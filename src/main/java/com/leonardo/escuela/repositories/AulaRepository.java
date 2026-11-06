package com.leonardo.escuela.repositories;

import com.leonardo.escuela.entities.Aula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AulaRepository extends JpaRepository<Aula, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdAulaNot(
            String nombre, Long idAula
    );

}
