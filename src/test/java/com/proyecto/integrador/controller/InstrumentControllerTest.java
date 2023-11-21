
package com.proyecto.integrador.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.proyecto.integrador.config.JwtUtil;
import com.proyecto.integrador.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.service.InstrumentService;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@WebMvcTest(InstrumentController.class)
class InstrumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstrumentService instrumentService;

    @MockBean
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createInstrument() {
        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setId(1L);
        instrumentDto.setName("Guitar");
        instrumentDto.setDetail("Acoustic Guitar");
        instrumentDto.setScore(4.5);
        instrumentDto.setUploadDate(LocalDate.now());
        instrumentDto.setUpdateDate(LocalDate.now());
        instrumentDto.setAvailable(true);
        instrumentDto.setDeleted(false);

        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setName("Guitar");
        instrument.setDetail("Acoustic Guitar");
        instrument.setScore(4.5);
        instrument.setUploadDate(LocalDate.now());
        instrument.setUpdateDate(LocalDate.now());
        instrument.setAvailable(true);
        instrument.setDeleted(false);

        Mockito.when(instrumentService.createInstrument(any(InstrumentDto.class), anyList())).thenReturn(instrument);


        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/instruments")
                            .file("images", "testImage.jpg".getBytes())
                            .param("instrument", new ObjectMapper().writeValueAsString(instrumentDto)))
                    .andExpect(status().isOk())
                    .andReturn();


            Mockito.verify(instrumentService, Mockito.times(1)).createInstrument(any(InstrumentDto.class), anyList());


            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getTenInstruments() {

        List<Instrument> instruments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Instrument instrument = new Instrument();
            instrument.setId((long) i);

            instruments.add(instrument);
        }


        Mockito.when(instrumentService.getTenInstruments()).thenReturn(instruments);


        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/instruments"))
                    .andExpect(status().isOk())
                    .andReturn();


            Mockito.verify(instrumentService, Mockito.times(1)).getTenInstruments();


            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getAll() {

        List<Instrument> instrumentList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Instrument instrument = new Instrument();
            instrument.setId((long) i);

            instrumentList.add(instrument);
        }


        Page<Instrument> instrumentPage = new PageImpl<>(instrumentList);


        Mockito.when(instrumentService.getAll(any(Pageable.class))).thenReturn(instrumentPage);


        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/instruments/paginated")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andReturn();


            Mockito.verify(instrumentService, Mockito.times(1)).getAll(any(Pageable.class));


            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getInstrumentById() {
        Long id = 1L;


        Instrument instrument = new Instrument();
        instrument.setId(id);



        Mockito.when(instrumentService.getInstrumentById(id)).thenReturn(instrument);


        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/instruments/id/" + id))
                    .andExpect(status().isOk())
                    .andReturn();


            Mockito.verify(instrumentService, Mockito.times(1)).getInstrumentById(id);


            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateInstrument() {

        try {
            Long id = 1L;
            InstrumentDto instrumentDto = new InstrumentDto();
            instrumentDto.setId(id);
            instrumentDto.setName("Guitar");
            instrumentDto.setDetail("Acoustic Guitar");
            instrumentDto.setScore(4.5);
            instrumentDto.setUploadDate(LocalDate.now());
            instrumentDto.setUpdateDate(LocalDate.now());
            instrumentDto.setAvailable(true);
            instrumentDto.setDeleted(false);

            Instrument instrument = new Instrument();
            instrument.setId(id);
            instrument.setName("Guitar");
            instrument.setDetail("Acoustic Guitar");
            instrument.setScore(4.5);
            instrument.setUploadDate(LocalDate.now());
            instrument.setUpdateDate(LocalDate.now());
            instrument.setAvailable(true);
            instrument.setDeleted(false);

            Mockito.when(instrumentService.updateInstrument(id, instrumentDto)).thenReturn(instrument);

            mockMvc.perform(MockMvcRequestBuilders.put("/instruments/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(instrumentDto))
                            .with(csrf()))
                    .andExpect(status().isOk())
                    .andReturn();

            Mockito.verify(instrumentService, Mockito.times(1)).updateInstrument(id, instrumentDto);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void deleteInstrument() {
        try{
        Long id = 1L;

        Mockito.doNothing().when(instrumentService).deleteInstrument(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/instruments/" + id)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(instrumentService, Mockito.times(1)).deleteInstrument(id);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser(username = "test", roles = {"Admin"})
    void getName() throws Exception {
        String name = "Guitar";
        Pageable pageable = PageRequest.of(0, 10);

        List<Instrument> instruments = new ArrayList<>();

        Page<Instrument> instrumentPage = new PageImpl<>(instruments, pageable, instruments.size());

        Mockito.when(instrumentService.getName(name, pageable)).thenReturn(instrumentPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/instruments/" + name)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        Mockito.verify(instrumentService, Mockito.times(1)).getName(name, pageable);
    }
}