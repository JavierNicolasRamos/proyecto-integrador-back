package com.proyecto.integrador.dto;

import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private User user;
    private Instrument instrument;
    private Boolean activeBooking;
    private LocalDate bookingStart;
    private LocalDate bookingEnd;
    private Boolean deleted;

}
