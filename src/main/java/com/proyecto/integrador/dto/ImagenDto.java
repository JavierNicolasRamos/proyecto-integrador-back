package com.proyecto.integrador.dto;

import com.proyecto.integrador.entity.Instrumento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImagenDto {
    private Long id;
    private String imagen;
    private Instrumento instrumento;
}
