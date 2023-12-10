package com.proyecto.integrador.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.config.JwtUtil;

import com.proyecto.integrador.dto.BookingDto;
import com.proyecto.integrador.entity.Booking;

import com.proyecto.integrador.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @MockBean
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void listBooking() throws  Exception {
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();

        List<Booking> mockBookings = Arrays.asList(booking1, booking2);


        when(bookingService.listBooking()).thenReturn(mockBookings);


        mockMvc.perform(get("/booking")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(booking1.getId()))
                .andExpect(jsonPath("$[1].id").value(booking2.getId()));

    }


    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getBooking() throws  Exception{
        Long id = 1L;
        Booking mockBooking = new Booking();
        mockBooking.setId(id);


        when(bookingService.getBooking(id)).thenReturn(mockBooking);

        mockMvc.perform(get("/booking/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id));

        verify(bookingService).getBooking(id);
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void createBooking() throws  Exception{
        BookingDto bookingDto = new BookingDto();


        Booking mockBooking = new Booking();
        mockBooking.setId(1L);

        when(bookingService.createBooking(bookingDto)).thenReturn(mockBooking);

        mockMvc.perform(post("/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService).createBooking(any(BookingDto.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void updateBooking() throws Exception{
        BookingDto bookingDto = new BookingDto();


        Booking updatedBooking = new Booking();
        updatedBooking.setId(1L);

        when(bookingService.updateBooking(bookingDto)).thenReturn(updatedBooking);

        mockMvc.perform(put("/booking")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

        verify(bookingService).updateBooking(any(BookingDto.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void deleteBooking() throws Exception{
        Long id = 1L;

        doNothing().when(bookingService).deleteBooking(id);

        mockMvc.perform(delete("/booking/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Reserva eliminada"));

        verify(bookingService).deleteBooking(id);
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void findOccupiedDates() throws Exception{
        Long instrumentId = 1L;
        List<LocalDate> occupiedDates = Arrays.asList(LocalDate.of(2023, 12, 25), LocalDate.of(2023, 12, 31));

        when(bookingService.findOccupiedDates(instrumentId)).thenReturn(occupiedDates);

        mockMvc.perform(get("/booking/occupied-dates/" + instrumentId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("2023-12-25"))
                .andExpect(jsonPath("$[1]").value("2023-12-31"));

        verify(bookingService).findOccupiedDates(instrumentId);
    }



}