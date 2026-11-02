package com.leonardo.escuela.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "GRUPOS",
        schema = "ESCUELA",
        uniqueConstraints = @UniqueConstraint(
                name = "GRUPO_CU_MA_AU_PE_UK",
                columnNames = {
                        "ID_CURSO", "ID_MAESTRO",
                        "ID_AULA", "PERIODO"
                }
        )
)
@Getter
@Setter
@NoArgsConstructor
public class Grupo {

    @Id
    @Column(name = "ID_GRUPO")
    private Long idGrupo;

    // Relación con Curso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    // Relación con Maestro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    // Relación con Aula
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", nullable = false, length = 20)
    private String periodo;
}