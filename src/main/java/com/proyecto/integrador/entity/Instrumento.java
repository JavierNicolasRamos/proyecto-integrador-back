package com.proyecto.integrador.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "INSTRUMENTOS")
@Getter
@Setter
@NoArgsConstructor
public class Instrumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private LocalDate inicioReserva;
    private LocalDate finReserva;

    @OneToOne
    private Usuario usuarioReserva;

    private LocalDate fechaCarga;
    private LocalDate fechaDelete;
    private LocalDate fechaUpdate;
    private Boolean eliminado;


}
