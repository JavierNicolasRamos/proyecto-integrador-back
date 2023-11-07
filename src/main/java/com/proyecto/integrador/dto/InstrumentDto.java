package com.proyecto.integrador.dto;

import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Booking;
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
    private Category category;
    private List<ImageDto> image;
    private Double score;
    private List<Booking> bookings;
    private LocalDate uploadDate;
    private LocalDate updateDate;
    private Boolean available;
    private Boolean deleted;
}
