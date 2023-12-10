package com.proyecto.integrador.controller;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.integrador.dto.FavouriteDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.service.FavouriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FavouriteControllerTest {

    @InjectMocks
    private FavouriteController favouriteController;

    @Mock
    private FavouriteService favouriteService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(favouriteController).build();
    }

    @Test
    void addFavourite() throws Exception {
        FavouriteDto favouriteDto = new FavouriteDto();


        List<Instrument> expectedInstruments = new ArrayList<>();


        when(favouriteService.addFavourite(any())).thenReturn(expectedInstruments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/favourite/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favouriteDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Instrument[] responseInstruments = new ObjectMapper().readValue(responseBody, Instrument[].class);
        assertEquals(expectedInstruments, Arrays.asList(responseInstruments));
    }

    @Test
    void removeFavourite() throws Exception{
        FavouriteDto favouriteDto = new FavouriteDto();


        List<Instrument> expectedInstruments = new ArrayList<>();


        when(favouriteService.removeFavourite(any())).thenReturn(expectedInstruments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/favourite/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(favouriteDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Instrument[] responseInstruments = new ObjectMapper().readValue(responseBody, Instrument[].class);
        assertEquals(expectedInstruments, Arrays.asList(responseInstruments));
    }

    @Test
    void getFavourites() throws Exception{
        String email = "test@example.com";

        List<Instrument> expectedInstruments = new ArrayList<>();

        when(favouriteService.getFavourites(anyString())).thenReturn(expectedInstruments);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/favourite")
                        .param("email", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Instrument[] responseInstruments = new ObjectMapper().readValue(responseBody, Instrument[].class);
        assertEquals(expectedInstruments, Arrays.asList(responseInstruments));
    }
}