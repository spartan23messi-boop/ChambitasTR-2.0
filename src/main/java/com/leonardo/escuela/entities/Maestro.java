package com.leonardo.escuela.entities;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@Table(name = "MAESTROS", schema = "ESCUELA")
@Getter
@Setter
@NoArgsConstructor
public class Maestro {

    @Id
    @Column(name = "ID_MAESTRO")
    private Long idMaestro;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "TELEFONO", nullable = false, unique = true, length = 10)
    private String telefono;

    /*@Builder.Default
    @OneToMany(mappedBy = "maestro", fetch = FetchType.LAZY)
    private Lis=<Grupo> grupos = new ArrayList<>();*/
}
