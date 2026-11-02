package com.leonardo.escuela.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CALIFICACIONES", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
public class Calificacion {

    @Id
    @Column(name = "ID_CALIFICACION")
    private Long idCalificacion;

    // Una calificación por inscripción
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_INSCRIPCION",
            nullable = false,
            unique = true
    )
    private Inscripcion inscripcion;

    @Column(name = "CALIFICACION", nullable = false, precision = 3, scale = 1)
    private BigDecimal calificacion;

    @Column(name = "FECHA_REGISTRO")
    private LocalDate fechaRegistro;
}