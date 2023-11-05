package com.proyecto.integrador.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CaracteristicaDto {
    private Long id;
    private String nombre;
    private String icono;
    private Boolean eliminada;
}
