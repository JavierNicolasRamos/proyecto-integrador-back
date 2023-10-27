package com.proyecto.integrador.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "RESERVAS")
@Getter
@Setter
@NoArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "El campo 'usuario' no puede ser nulo")
    @ManyToOne
    private Usuario usuario;

    @NotNull(message = "El campo 'proveedor' no puede ser nulo")
    @ManyToOne
    private Usuario proveedor;

    @NotNull(message = "El campo 'instrumento' no puede ser nulo")
    @ManyToOne
    private Instrumento instrumento;

    private Boolean reservaActiva;
    private LocalDate inicioReserva;
    private LocalDate finReserva;
    private Boolean eliminado;
}
