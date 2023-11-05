package com.proyecto.integrador.dto;

import com.proyecto.integrador.entity.Categoria;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.entity.Imagen;
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
    private List<ImagenDto> imagen;
    private List<Reserva> reservas;
    private Boolean eliminado;
    private Double puntuacion;
    private String detalle;
    private List<CaracteristicaDto> caracteristicas;
}
