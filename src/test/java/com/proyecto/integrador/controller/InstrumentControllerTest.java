/*
package com.proyecto.integrador.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(InstrumentController.class)
class InstrumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstrumentService instrumentService;

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
    void createImagesInstrument(){
        Long id = 1L;
        MultipartFile image1 = new MockMultipartFile("image1", "Hello, World!".getBytes());
        MultipartFile image2 = new MockMultipartFile("image2", "Hello, World!".getBytes());
        List<MultipartFile> images = Arrays.asList(image1, image2);


        Image image = new Image();
        image.setId(1L);
        image.setImage("Image Data");
        image.setDeleted(false);


        Category category = new Category();
        category.setId(1L);
        category.setName("Category Name");
        category.setDetails("Category Details");


        Characteristic characteristic = new Characteristic();
        characteristic.setId(1L);
        characteristic.setName("Characteristic Name");
        characteristic.setIcon("Characteristic Icon");
        characteristic.setDeleted(false);


        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setName("Instrument Name");
        instrument.setDetail("Instrument Detail");
        instrument.setScore(4.5);
        instrument.setUploadDate(LocalDate.now());
        instrument.setUpdateDate(LocalDate.now());
        instrument.setAvailable(true);
        instrument.setDeleted(false);
        instrument.setCategory(category);
        instrument.setImage(Collections.singletonList(image));
        instrument.setCharacteristics(Collections.singletonList(characteristic));




        Mockito.when(instrumentService.createImagesInstrument(id, images)).thenReturn(instrument);

        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/instruments/createimages")
                            .file("images", "testImage.jpg".getBytes())
                            .part(new MockPart("id", id.toString().getBytes())))
                    .andExpect(status().isOk())
                    .andReturn();


            Mockito.verify(instrumentService, Mockito.times(1)).createImagesInstrument(id, images);


            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
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
    }

    @Test
    void deleteInstrument() {
    }

    @Test
    void getName() {
    }
}
*/

