package com.leonardo.escuela.repositories;

import com.leonardo.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    boolean existsByMaestroIdMaestro(Long idMaestro);


    }


