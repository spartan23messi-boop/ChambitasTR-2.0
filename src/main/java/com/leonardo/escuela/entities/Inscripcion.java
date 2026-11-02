package com.leonardo.escuela.entities;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "INSCRIPCIONES",
        schema = "ESCUELA",
        uniqueConstraints = @UniqueConstraint(
                name = "INSCRIPCION_ALU_GRU_UK",
                columnNames = {"ID_ALUMNO", "ID_GRUPO"}
        )
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long idInscripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Builder.Default
    @Column(name = "FECHA_INSCRIPCION", nullable = false)
    private LocalDate fechaInscripcion=LocalDate.now();

    @OneToOne(mappedBy = "Inscripcion")
    private Calificacion calificacion;
}