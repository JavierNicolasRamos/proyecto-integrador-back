package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CategoryDto;
import com.proyecto.integrador.dto.CharacteristicDto;
import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Characteristic;
import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.exception.DeleteInstrumentException;
import com.proyecto.integrador.repository.InstrumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
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

    @Mock
    private CategoryService categoryService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createInstrument() {

        CharacteristicDto characteristicDto = new CharacteristicDto();
        characteristicDto.setName("TestCharacteristic");
        List<CharacteristicDto> characteristics = Collections.singletonList(characteristicDto);

        InstrumentDto instrumentDto = new InstrumentDto();
        instrumentDto.setName("TestInstrument");


        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        instrumentDto.setCategoryDto(categoryDto);

        instrumentDto.setCharacteristics(characteristics);
        instrumentDto.setUploadDate(LocalDate.now());
        instrumentDto.setUpdateDate(LocalDate.now());
        instrumentDto.setDetail("TestDetail");
        instrumentDto.setAvailable(true);
        instrumentDto.setScore(0.0);


        instrumentDto.setCharacteristics(characteristics);

        List<MultipartFile> multipartFiles = Collections.singletonList(mock(MultipartFile.class));

        when(instrumentRepository.getByName("TestInstrument")).thenReturn(Optional.empty());

        when(categoryService.categoryById(anyLong())).thenReturn(new Category());

        Instrument result = instrumentService.createInstrument(instrumentDto, multipartFiles);

        assertNotNull(result);
        assertEquals("TestInstrument", result.getName());

        verify(instrumentRepository, times(1)).getByName("TestInstrument");
        verify(instrumentRepository, times(1)).save(any(Instrument.class));
        verify(characteristicService, times(1)).associateCharacteristic(any(Instrument.class), eq(characteristics));
    }

    @Test
    void getTenInstruments() {
        List<Instrument> simulatedInstruments = Arrays.asList(
                new Instrument(), new Instrument(), new Instrument(), new Instrument(), new Instrument(),
                new Instrument(), new Instrument(), new Instrument(), new Instrument(), new Instrument()
        );

        when(instrumentRepository.findRandomInstruments()).thenReturn(simulatedInstruments);

        List<Instrument> result = instrumentService.getTenInstruments();

        assertNotNull(result);

        assertEquals(10, result.size());

        verify(instrumentRepository, times(1)).findRandomInstruments();
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

        Characteristic characteristic = new Characteristic();
        characteristic.setName("TestCharacteristic");
        List<Characteristic> characteristics = Collections.singletonList(characteristic);

        CharacteristicDto characteristicDto = new CharacteristicDto();
        characteristicDto.setName("TestCharacteristic");
        List<CharacteristicDto> characteristicsDto = Collections.singletonList(characteristicDto);


        Instrument existingInstrument = new Instrument();
        existingInstrument.setId(1L);
        existingInstrument.setName("ExistingInstrument");
        existingInstrument.setCategory(new Category());
        existingInstrument.setUploadDate(LocalDate.now());
        existingInstrument.setUpdateDate(LocalDate.now());
        existingInstrument.setDetail("TestDetail");
        existingInstrument.setAvailable(true);
        existingInstrument.setScore(0.0);
        existingInstrument.setBookings(Collections.emptyList());
        existingInstrument.setDeleted(false);
        existingInstrument.setCharacteristics(characteristics);
        existingInstrument.setImage(Collections.emptyList());

        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(existingInstrument));
        when(instrumentRepository.save(any())).thenReturn(existingInstrument);


        InstrumentDto updatedDto = new InstrumentDto();
        updatedDto.setId(1L);
        updatedDto.setName("UpdatedInstrument");
        updatedDto.setUploadDate(LocalDate.now());
        updatedDto.setUpdateDate(LocalDate.now());
        updatedDto.setDetail("TestDetailNew");
        updatedDto.setAvailable(true);
        updatedDto.setScore(5.0);
        updatedDto.setDeleted(false);
        updatedDto.setCharacteristics(characteristicsDto);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        updatedDto.setCategoryDto(categoryDto);


        when(categoryService.categoryById(anyLong())).thenReturn(new Category());




        Instrument result = instrumentService.updateInstrument(1L, updatedDto);


        assertNotNull(result);


        assertEquals(1L, result.getId());
        assertEquals("UpdatedInstrument", result.getName());
        assertEquals("TestDetailNew", result.getDetail());
        assertEquals(5.0, result.getScore());


        verify(instrumentRepository, times(1)).findById(1L);
        verify(instrumentRepository, times(1)).save(any());
        verify(characteristicService, times(1)).associateCharacteristic(any(), any());
        verify(categoryService, times(1)).categoryById(anyLong());
    }


    @Test
    void deleteInstrument_ExistingInstrument_DeletesInstrument() {

        Image testImage = new Image();
        testImage.setId(1L);


        Instrument existingInstrument = new Instrument();
        existingInstrument.setId(1L);
        existingInstrument.setName("TestInstrument");
        existingInstrument.setImage(Collections.singletonList(testImage));


        when(instrumentRepository.findById(1L)).thenReturn(Optional.of(existingInstrument));


        doNothing().when(imageService).deleteImage(anyLong());


        try {
            instrumentService.deleteInstrument(1L);
        } catch (DeleteInstrumentException e) {
            fail("Error al eliminar el instrumento: " + e.getMessage());
        }


        verify(instrumentRepository, times(1)).findById(1L);
        verify(instrumentRepository, times(1)).save(existingInstrument);


        verify(imageService, times(1)).deleteImage(eq(testImage.getId()));


        assertTrue(existingInstrument.getDeleted());
    }


    @Test
    void getAll() {
        List<Instrument> simulatedInstruments = Arrays.asList(
                new Instrument(), new Instrument(), new Instrument()
        );

        Page<Instrument> simulatedPage = new PageImpl<>(simulatedInstruments);

        when(instrumentRepository.getAll(any(Pageable.class))).thenReturn(simulatedPage);

        Page<Instrument> result = instrumentService.getAll(mock(Pageable.class));

        assertNotNull(result);

        assertEquals(simulatedInstruments, result.getContent());

        verify(instrumentRepository, times(1)).getAll(any(Pageable.class));
    }

    @Test
    void getName() {
        Pageable pageable = mock(Pageable.class);

        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setName("TestInstrument");

        when(instrumentRepository.getName("TestInstrument", pageable)).thenReturn(new PageImpl<>(Collections.singletonList(instrument)));

        Page<Instrument> result = instrumentService.getName("TestInstrument", pageable);

        assertNotNull(result);

        assertEquals(1, result.getContent().size());
        assertEquals("TestInstrument", result.getContent().get(0).getName());

        verify(instrumentRepository).getName("TestInstrument", pageable);
    }
}