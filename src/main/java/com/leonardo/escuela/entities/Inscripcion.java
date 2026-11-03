
package com.leonardo.escuela.entities;

import com.leonardo.escuela.exceptions.DatoInvalidoException;
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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long idInscripcion;

    // ALUMNO QUE REALIZA LA INSCRIPCIÓN
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    // GRUPO AL QUE SE INSCRIBE
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Builder.Default
    @Column(name = "FECHA_INSCRIPCION")
    private LocalDate fechaInscripcion = LocalDate.now();

    // UNA INSCRIPCIÓN PUEDE TENER UNA CALIFICACIÓN
    @OneToOne(mappedBy = "inscripcion", fetch = FetchType.LAZY)
    private Calificacion calificacion;

    // VALIDAR ALUMNO Y GRUPO
    private static void validarDatos(Alumno alumno, Grupo grupo) {
        if (alumno == null || grupo == null)
            throw new DatoInvalidoException("El alumno y el grupo son requeridos");
    }

    // CREAR INSCRIPCIÓN
    public static Inscripcion crear(Alumno alumno, Grupo grupo) {
        validarDatos(alumno, grupo);
        return Inscripcion.builder().alumno(alumno).grupo(grupo).build();
    }

    // ACTUALIZAR INSCRIPCIÓN (SIN MODIFICAR SU FECHA)
    public void actualizar(Alumno alumno, Grupo grupo) {
        validarDatos(alumno, grupo);
        this.alumno = alumno;
        this.grupo = grupo;
    }


}
