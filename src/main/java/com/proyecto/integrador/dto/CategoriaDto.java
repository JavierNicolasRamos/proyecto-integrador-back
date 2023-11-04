package com.proyecto.integrador.dto;

import com.proyecto.integrador.entity.Imagen;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CategoriaDto {
    private Long id;
    private String descripcion;
    private ImagenDto imagen; //Get imagen devuelve una imagenDto
}
