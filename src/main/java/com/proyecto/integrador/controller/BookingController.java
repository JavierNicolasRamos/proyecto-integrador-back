package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.BookingDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<Booking> listBooking() {
        return bookingService.listBooking();
    }

    @GetMapping("/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBooking(id);
    }

    @PostMapping
    public Booking createBooking(@RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookingDto);
    }

    @PutMapping
    public Booking updateBooking(@RequestBody BookingDto bookingDto) {
        return bookingService.updateBooking(bookingDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }
}
