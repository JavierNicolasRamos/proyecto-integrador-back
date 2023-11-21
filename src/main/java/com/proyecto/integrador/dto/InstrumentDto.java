package com.proyecto.integrador.dto;

import com.proyecto.integrador.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class InstrumentDto {
    private Long id;
    private String name;
    private String detail;
    private List<CharacteristicDto> characteristics;
    private UserDto seller;
    private CategoryDto categoryDto;
    private Double score;
    private LocalDate uploadDate;
    private LocalDate updateDate;
    private Boolean available;
    private Boolean deleted;
}
