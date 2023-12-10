package com.proyecto.integrador.controller;


import com.fasterxml.jackson.databind.ObjectMapper;


import com.proyecto.integrador.dto.CharacteristicDto;
import com.proyecto.integrador.entity.*;
import com.proyecto.integrador.service.CharacteristicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@SpringBootTest
class CharacteristicControllerTest {
    @InjectMocks
    private CharacteristicController characteristicController;

    @Mock
    private CharacteristicService characteristicService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(characteristicController).build();
    }

    @Test
    void createCharacteristic() throws Exception {

        CharacteristicDto characteristicDto = new CharacteristicDto();
        characteristicDto.setName("Test Characteristic");


        Characteristic characteristicDePrueba = new Characteristic();
        characteristicDePrueba.setId(1L);
        characteristicDePrueba.setName(characteristicDto.getName());

        Mockito.when(characteristicService.createCharacteristic(any(CharacteristicDto.class)))
                .thenReturn(characteristicDePrueba);


        MvcResult result = mockMvc.perform(post("/characteristic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicDto)))
                .andExpect(status().isOk())
                .andReturn();


        Mockito.verify(characteristicService, Mockito.times(1)).createCharacteristic(any(CharacteristicDto.class));


        String responseBody = result.getResponse().getContentAsString();
        Characteristic characteristicEnRespuesta = objectMapper.readValue(responseBody, Characteristic.class);


        assertEquals(characteristicDePrueba.getId(), characteristicEnRespuesta.getId());
        assertEquals(characteristicDePrueba.getName(), characteristicEnRespuesta.getName());

    }




    @Test
    void editCharacteristic() throws Exception{

        Long characteristicId = 1L;
        CharacteristicDto characteristicDto = new CharacteristicDto();
        characteristicDto.setName("Updated Characteristic");


        Characteristic characteristicDePrueba = new Characteristic();
        characteristicDePrueba.setId(characteristicId);
        characteristicDePrueba.setName(characteristicDto.getName());

        Mockito.when(characteristicService.editCharacteristic(any(Long.class), any(CharacteristicDto.class)))
                .thenReturn(characteristicDePrueba);


        MvcResult result = mockMvc.perform(put("/characteristic/{id}", characteristicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(characteristicDto)))
                .andExpect(status().isOk())
                .andReturn();


        Mockito.verify(characteristicService, Mockito.times(1))
                .editCharacteristic(Mockito.eq(characteristicId), Mockito.eq(characteristicDto));


        String responseBody = result.getResponse().getContentAsString();
        Characteristic characteristicEnRespuesta = objectMapper.readValue(responseBody, Characteristic.class);


        assertEquals(characteristicDePrueba.getId(), characteristicEnRespuesta.getId());
        assertEquals(characteristicDePrueba.getName(), characteristicEnRespuesta.getName());

    }

    @Test
    void deleteCharacteristic() throws Exception{
        Long characteristicId = 1L;

        Mockito.doNothing().when(characteristicService).deleteCharacteristic(characteristicId);


        MvcResult result = mockMvc.perform(delete("/characteristic/{id}", characteristicId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        Mockito.verify(characteristicService, Mockito.times(1))
                .deleteCharacteristic(Mockito.eq(characteristicId));


        String responseBody = result.getResponse().getContentAsString();
        assertEquals("La characterística con ID " + characteristicId + " se eliminó de manera exitosa", responseBody);
    }

    @Test
    void listCharacteristic() throws Exception{

        Characteristic characteristic1 = new Characteristic();
        Characteristic characteristic2 = new Characteristic();
        List<Characteristic> characteristicList = Arrays.asList(characteristic1, characteristic2);

        Mockito.when(characteristicService.listCharacteristic()).thenReturn(characteristicList);

        MvcResult result = mockMvc.perform(get("/characteristic/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String responseBody = result.getResponse().getContentAsString();
        Characteristic[] characteristicArrayEnRespuesta = objectMapper.readValue(responseBody, Characteristic[].class);


        List<Characteristic> characteristicListEnRespuesta = Arrays.asList(characteristicArrayEnRespuesta);


        assertEquals(characteristicList.size(), characteristicListEnRespuesta.size());
        assertEquals(characteristicList.get(0).getId(), characteristicListEnRespuesta.get(0).getId());
        assertEquals(characteristicList.get(0).getName(), characteristicListEnRespuesta.get(0).getName());


    }
}