package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CharacteristicDto;
import com.proyecto.integrador.entity.Characteristic;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.exception.DeleteCharacteristicException;
import com.proyecto.integrador.repository.CharacteristicRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CharacteristicServiceTest {

    @InjectMocks
    private CharacteristicService characteristicService;

    @Mock
    private CharacteristicRepository characteristicRepository;

    @Mock
    private InstrumentRepository instrumentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCharacteristic() {
        CharacteristicDto characteristicDto = new CharacteristicDto();
        characteristicDto.setName("TestCharacteristic");
        characteristicDto.setIcon("TestIcon");


        when(characteristicRepository.findByName(characteristicDto.getName())).thenReturn(Optional.empty());


        Characteristic savedCharacteristic = new Characteristic();
        savedCharacteristic.setId(1L);
        savedCharacteristic.setName(characteristicDto.getName());
        savedCharacteristic.setIcon(characteristicDto.getIcon());
        when(characteristicRepository.save(any(Characteristic.class))).thenReturn(savedCharacteristic);


        Characteristic result = characteristicService.createCharacteristic(characteristicDto);


        assertNotNull(result);


        verify(characteristicRepository, times(1)).findByName(characteristicDto.getName());
        verify(characteristicRepository, times(1)).save(any(Characteristic.class));


        assertEquals(characteristicDto.getName(), result.getName());
        assertEquals(characteristicDto.getIcon(), result.getIcon());
        assertEquals(savedCharacteristic.getId(), result.getId());
    }

    @Test
    void editCharacteristic() {

        Long characteristicId = 1L;
        CharacteristicDto updatedCharacteristicDto = new CharacteristicDto();
        updatedCharacteristicDto.setName("UpdatedCharacteristic");
        updatedCharacteristicDto.setIcon("UpdatedIcon");

        Characteristic existingCharacteristic = new Characteristic();
        existingCharacteristic.setId(characteristicId);
        existingCharacteristic.setName("OldName");
        existingCharacteristic.setIcon("OldIcon");


        when(characteristicRepository.findById(characteristicId)).thenReturn(Optional.of(existingCharacteristic));
        when(characteristicRepository.save(any(Characteristic.class))).thenReturn(existingCharacteristic);


        Characteristic result = characteristicService.editCharacteristic(characteristicId, updatedCharacteristicDto);


        assertEquals(characteristicId, result.getId());
        assertEquals(updatedCharacteristicDto.getName(), result.getName());
        assertEquals(updatedCharacteristicDto.getIcon(), result.getIcon());


        verify(characteristicRepository, times(1)).findById(characteristicId);
        verify(characteristicRepository, times(1)).save(existingCharacteristic);
    }

    @Test
    void deleteCharacteristic() {

        Long characteristicId = 1L;
        Characteristic existingCharacteristic = new Characteristic();
        existingCharacteristic.setId(characteristicId);


        when(characteristicRepository.findById(characteristicId)).thenReturn(Optional.of(existingCharacteristic));
        when(characteristicRepository.save(any(Characteristic.class))).thenReturn(existingCharacteristic);


        try {
            characteristicService.deleteCharacteristic(characteristicId);
        } catch (DeleteCharacteristicException e) {
            fail("Error al eliminar la característica: " + e.getMessage());
        }


        verify(characteristicRepository, times(1)).findById(characteristicId);
        verify(characteristicRepository, times(1)).save(existingCharacteristic);


        assertTrue(existingCharacteristic.getDeleted());
    }

    @Test
    void listCharacteristic() {
        Characteristic characteristic1 = new Characteristic();
        characteristic1.setId(1L);
        characteristic1.setName("Characteristic1");
        characteristic1.setIcon("Icon1");

        Characteristic characteristic2 = new Characteristic();
        characteristic2.setId(2L);
        characteristic2.setName("Characteristic2");
        characteristic2.setIcon("Icon2");

        List<Characteristic> characteristicList = Arrays.asList(characteristic1, characteristic2);


        when(characteristicRepository.findAll()).thenReturn(characteristicList);


        List<Characteristic> result = characteristicService.listCharacteristic();


        assertEquals(characteristicList, result);
    }

    @Test
    void associateCharacteristic() {
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        instrument.setCharacteristics(new ArrayList<>());

        CharacteristicDto characteristicDto1 = new CharacteristicDto();
        characteristicDto1.setId(1L);

        CharacteristicDto characteristicDto2 = new CharacteristicDto();
        characteristicDto2.setId(2L);

        List<CharacteristicDto> characteristicDtos = Arrays.asList(characteristicDto1, characteristicDto2);

        Characteristic characteristic1 = new Characteristic();
        characteristic1.setId(1L);

        Characteristic characteristic2 = new Characteristic();
        characteristic2.setId(2L);

        List<Characteristic> existingCharacteristics = Arrays.asList(characteristic1, characteristic2);


        when(characteristicRepository.findById(1L)).thenReturn(Optional.of(characteristic1));
        when(characteristicRepository.findById(2L)).thenReturn(Optional.of(characteristic2));


        when(instrumentRepository.save(instrument)).thenReturn(instrument);


        characteristicService.associateCharacteristic(instrument, characteristicDtos);


        assertEquals(existingCharacteristics, instrument.getCharacteristics());


        verify(characteristicRepository, times(1)).findById(1L);
        verify(characteristicRepository, times(1)).findById(2L);
        verify(instrumentRepository, times(1)).save(instrument);
    }
}