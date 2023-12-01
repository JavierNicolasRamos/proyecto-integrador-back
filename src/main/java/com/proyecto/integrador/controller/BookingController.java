package com.proyecto.integrador.controller;

import com.proyecto.integrador.dto.BookingDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<Booking>> listBooking() {
        return ResponseEntity.ok(bookingService.listBooking());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookingService.getBooking(id));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDto));
    }

    @PutMapping
    public ResponseEntity<Booking> updateBooking(@RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.updateBooking(bookingDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable("id") Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Reserva eliminada");
    }

    @GetMapping("/occupied-dates/{instrumentId}")
    public ResponseEntity<List<LocalDate>> findOccupiedDates(@PathVariable("instrumentId") Long instrumentId) {
        List<LocalDate> occupiedDates = bookingService.findOccupiedDates(Long.valueOf(instrumentId));
        return ResponseEntity.ok(occupiedDates);
    }
}
