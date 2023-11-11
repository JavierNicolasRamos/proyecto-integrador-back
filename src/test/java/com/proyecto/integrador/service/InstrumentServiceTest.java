package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.repository.CategoryRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class InstrumentServiceTest {

    @InjectMocks
    private InstrumentService instrumentService;
    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private CharacteristicService characteristicService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInstrument() {
        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setName("TestInstrument");
        instrumentDto.setCategory(new Category());
        instrumentDto.setUploadDate(LocalDate.now());
        instrumentDto.setUpdateDate(LocalDate.now());
        instrumentDto.setDetail("TestDetail");
        instrumentDto.setAvailable(true);
        instrumentDto.setScore(0.0);
        instrumentDto.setCharacteristics(Collections.emptyList());
        instrumentDto.setImage(Collections.emptyList());
        when(instrumentRepository.getByName("TestInstrument")).thenReturn(Optional.empty());




        Instrument result = instrumentService.createInstrument(instrumentDto);
        assertNotNull(result);
        assertEquals("TestInstrument", result.getName());


        verify(instrumentRepository, times(1)).getByName("TestInstrument");
        verify(instrumentRepository, times(1)).save(any(Instrument.class));
        verify(imageService, times(1)).saveImagesInstrument(any(Instrument.class), any());
        verify(characteristicService, times(1)).associateCharacteristic(any(Instrument.class), any());
    }

    @Test
    void getTenInstruments() {

    }

    @Test
    void getInstrumentById() {

        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setName("Guitar");
        instrument.setDetail("Acoustic");
        instrument.setCharacteristics(Collections.emptyList());
        instrument.setCategory(new Category());
        instrument.setImage(Collections.emptyList());
        instrument.setScore(0.0);
        instrument.setBookings(Collections.emptyList());
        instrument.setUploadDate(LocalDate.now());
        instrument.setUpdateDate(LocalDate.now());
        instrument.setDeleted(false);
        instrument.setAvailable(true);
        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(instrument));

        Instrument result = instrumentService.getInstrumentById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Guitar", result.getName());
        assertEquals("Acoustic", result.getDetail());
        assertEquals(Collections.emptyList(), result.getCharacteristics());
        assertEquals(new Category(), result.getCategory());
        assertEquals(Collections.emptyList(), result.getImage());
        assertEquals(0.0, result.getScore());
        assertEquals(Collections.emptyList(), result.getBookings());
        assertEquals(LocalDate.now(), result.getUploadDate());
        assertEquals(LocalDate.now(), result.getUpdateDate());
        assertEquals(false, result.getDeleted());
        assertEquals(true, result.getAvailable());

        verify(instrumentRepository).findById(1L);

    }

    @Test
    void updateInstrument() {
    }

    @Test
    void deleteInstrument() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getName() {
    }
}