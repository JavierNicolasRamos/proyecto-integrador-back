package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.BookingDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.repository.BookingRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
class BookingServiceTest {


    @InjectMocks
    private BookingService bookingService;

    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private BookingRepository bookingRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking() {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setInstrument(new Instrument());
        bookingDto.getInstrument().setId(1L);

        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setAvailable(true);


        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));
        when(instrumentRepository.save(instrument)).thenReturn(instrument);


        Booking savedBooking = new Booking();
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);


        Booking result = bookingService.createBooking(bookingDto);


        assertNotNull(result);


        verify(instrumentRepository, times(1)).findById(1L);
        verify(instrumentRepository, times(1)).save(instrument);

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void getBooking() {
    }

    @Test
    void listBooking() {
        List<Booking> mockBookings = new ArrayList<>();
        mockBookings.add(new Booking());
        mockBookings.add(new Booking());

        when(bookingRepository.findAll()).thenReturn(mockBookings);


        List<Booking> result = bookingService.listBooking();


        assertNotNull(result);


        assertEquals(2, result.size());


        verify(bookingRepository, times(1)).findAll();
    }

    @Test
    void updateBooking() {   //TODO: problem with the seller
/*
        User existingUser = new User();
        existingUser.setId(1L);


        Booking existingBooking = new Booking();
        existingBooking.setId(1L);
        existingBooking.setUser(existingUser);
        existingBooking.setActiveBooking(true);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));


        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setAvailable(true);
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));
        when(instrumentRepository.save(any(Instrument.class))).thenReturn(instrument);


        BookingDto updateDto = new BookingDto();
        updateDto.setId(1L);
        updateDto.setUser(existingUser);
        updateDto.setActiveBooking(false);


        Booking updatedBooking = bookingService.updateBooking(updateDto);


        assertNotNull(updatedBooking);


        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));


        verify(instrumentRepository, times(1)).findById(1L);
        verify(instrumentRepository, times(1)).save(any(Instrument.class));


        assertFalse(updatedBooking.getActiveBooking());

        assertFalse(instrument.getAvailable());
       */

    }

    @Test
    void deleteBooking() {

        Booking existingBooking = new Booking();
        existingBooking.setId(1L);
        existingBooking.setInstrument(new Instrument());

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));


        Instrument instrument = new Instrument();
        instrument.setId(1L);
        when(instrumentRepository.save(any(Instrument.class))).thenReturn(instrument);


        bookingService.deleteBooking(1L);


        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));


        verify(instrumentRepository, times(1)).save(any(Instrument.class));


        assertTrue(existingBooking.getDeleted());


        assertTrue(existingBooking.getInstrument().getAvailable());
    }
}