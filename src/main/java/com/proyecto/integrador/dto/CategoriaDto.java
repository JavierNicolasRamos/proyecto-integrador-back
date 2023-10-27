package com.proyecto.integrador.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoriaDto {
    private Long id;
    private String descripcion;
    private Boolean eliminado;
}
