package com.proyecto.integrador.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private BuyerDto buyerDto;
    private InstrumentDto instrumentDto;
    private Boolean activeBooking;
    private LocalDate bookingStart;
    private LocalDate bookingEnd;
    private Boolean deleted;
}
