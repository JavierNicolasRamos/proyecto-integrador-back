package com.proyecto.integrador.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.entity.Usuario;
import com.proyecto.integrador.entity.Imagen;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class InstrumentoDto {
    private Long id;
    private String nombre;
    private Categoria categoria;
    private LocalDate fechaCarga;
    private LocalDate fechaUpdate;
    private Boolean disponible;
    private List<Imagen> imagen;
    private List<Reserva> reservas;
}
