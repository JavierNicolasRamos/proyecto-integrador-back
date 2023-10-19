package com.proyecto.integrador.dto;

import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.entity.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReservaDto {
    private Long id;
    private Usuario usuario;
    private Instrumento instrumento;
    private Boolean reservaActiva;
    private LocalDate inicioReserva;
    private LocalDate finReserva;
}
