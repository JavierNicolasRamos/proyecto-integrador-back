package com.proyecto.integrador.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDto {
    private Integer score;
    private String reviewName;
    private String reviewDescription;

}
