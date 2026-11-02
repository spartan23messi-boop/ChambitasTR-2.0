package com.leonardo.escuela.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "AULAS", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
public class Aula {

    @Id
    @Column(name = "ID_AULA")
    private Long idAula;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 30)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;
}
