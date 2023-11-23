package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.BookingDto;
import com.proyecto.integrador.dto.BuyerDto;
import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.entity.Category;
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

import java.time.LocalDate;
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
    private InstrumentService instrumentService;
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createBooking() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setAvailable(true);

        User user = new User();
        user.setEmail("test@example.com");

        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setId(1L);

        BuyerDto buyerDto = new BuyerDto();
        buyerDto.setEmail("testBuyer@gmail.com");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setInstrumentDto(instrumentDto);
        bookingDto.setBuyerDto(buyerDto);
        bookingDto.setId(1L);
        bookingDto.setActiveBooking(true);
        bookingDto.setBookingStart(LocalDate.now());
        bookingDto.setBookingEnd(LocalDate.now().plusDays(5));


        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));
        when(userService.findByEmail(any())).thenReturn(user);
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);


        Booking result = bookingService.createBooking(bookingDto);


        assertFalse(result.getInstrument().getAvailable());
        verify(instrumentRepository, times(1)).save(any());
        verify(bookingRepository, times(1)).save(any());
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


        User user = new User();
        user.setEmail("test@example.com");

        Category category = new Category();


        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setAvailable(true);
        instrument.setCategory(category);

        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setId(1L);

        BuyerDto buyerDto = new BuyerDto();
        buyerDto.setEmail("testBuyer@gmail.com");

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBuyerDto(buyerDto);
        bookingDto.setInstrumentDto(instrumentDto);
        bookingDto.setActiveBooking(true);
        bookingDto.setBookingStart(LocalDate.now());
        bookingDto.setBookingEnd(LocalDate.now().plusDays(5));


        Booking existingBooking = new Booking();
        existingBooking.setUser(user);
        existingBooking.setInstrument(instrument);
        existingBooking.setActiveBooking(false);
        existingBooking.setBookingStart(LocalDate.now());
        existingBooking.setBookingEnd(LocalDate.now().plusDays(3));


        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));
        when(userService.findByEmail(any())).thenReturn(user);
        when(instrumentService.getInstrumentById(anyLong())).thenReturn(instrument);
        when(instrumentRepository.findById(anyLong())).thenReturn(Optional.of(instrument));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(instrumentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);


        Booking result = bookingService.updateBooking(bookingDto);


        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(instrument, result.getInstrument());
        assertTrue(result.getActiveBooking());


        verify(bookingRepository, times(1)).findById(anyLong());
        verify(userService, times(1)).findByEmail(any());
        verify(instrumentService, times(1)).getInstrumentById(anyLong());
        verify(instrumentRepository, times(1)).save(any());
        verify(bookingRepository, times(1)).save(any());


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