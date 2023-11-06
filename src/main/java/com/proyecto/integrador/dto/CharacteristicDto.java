package com.proyecto.integrador.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CharacteristicDto {
    private Long id;
    private String name;
    private String icon;
    private Boolean deleted;
}
