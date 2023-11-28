package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CharacteristicDto;
import com.proyecto.integrador.entity.Characteristic;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.exception.*;
import com.proyecto.integrador.repository.CharacteristicRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CharacteristicService {

    private static final Logger logger = LogManager.getLogger(CharacteristicService.class);
    @Autowired
    private CharacteristicRepository characteristicRepository;
    @Autowired
    private InstrumentRepository instrumentRepository;

    @Transactional
    public Characteristic createCharacteristic(CharacteristicDto characteristicDto) {
        try {
            logger.info("Se va a crear la característica con nombre: {} y icono: {}", characteristicDto.getName(), characteristicDto.getIcon());


            if (characteristicRepository.findByName(characteristicDto.getName()).isPresent()) {
                throw new DuplicateCategoryException("Ya existe una categoría con el mismo nombre: " + characteristicDto.getName());
            }

            Characteristic characteristic = new Characteristic();
            characteristic.setDeleted(false);
            characteristic.setName(characteristicDto.getName());
            characteristic.setIcon(characteristicDto.getIcon());

            Characteristic newCharacteristic = characteristicRepository.save(characteristic);
            logger.info("La característica con ID {} ha sido creada exitosamente.", newCharacteristic.getId());

            return newCharacteristic;
            } catch (Exception e){
                throw new CreateCharacteristicException("Ocurrió un error al crear la característica", e);
            }
    }

    @Transactional
    public Characteristic editCharacteristic(Long id, CharacteristicDto characteristicDto) {
        try {
            Optional<Characteristic> characteristicOptional = characteristicRepository.findById(id);
            if (characteristicOptional.isPresent()) {
                Characteristic characteristic = characteristicOptional.get();
                logger.info("Se va a modificar la característica con ID: {}, atributo nombre: {}, icono: {}",
                        id, characteristic.getName(), characteristic.getIcon());

                if (thereIsACharacteristicWithTheSameName(id, characteristicDto.getName())) {
                    throw new DuplicateCharacteristicException("Ya existe otra característica con el mismo nombre: " + characteristicDto.getName());
                }

                characteristic.setName(characteristicDto.getName());
                characteristic.setIcon(characteristicDto.getIcon());

                characteristicRepository.save(characteristic);
                logger.info("La característica con ID: {} fue modificada correctamente, atributo nombre: {}, icono: {}",
                        characteristic.getId(), characteristic.getName(), characteristic.getIcon());

                return characteristic;
            } else {
                throw new EntityNotFoundException("No se encontró la característica con ID: " + id);
            }
        } catch (Exception e) {
            throw new EditCharacteristicException("Ocurrió un error al editar la característica", e);
        }
    }

    public List<Characteristic> listCharacteristic() {
        logger.info("Obteniendo la lista de características registradas.");
        List<Characteristic> characteristic = characteristicRepository.getAll();
        logger.info("Lista de características obtenida exitosamente. Cantidad de características: {}", characteristic.size());
        return characteristic;
    }

    private boolean thereIsACharacteristicWithTheSameName(Long id, String name) {
        Optional<Characteristic> existsCharacteristic = characteristicRepository.findByName(name);
        return existsCharacteristic.filter(characteristic -> !characteristic.getId().equals(id)).isPresent();
    }

    public void associateCharacteristic(Instrument instrument, @NotNull List<CharacteristicDto> characteristicDtos) {
        List<Characteristic> newCharacteristics = new ArrayList<>();

        for (CharacteristicDto characteristicDto : characteristicDtos) {
            try {
                Optional<Characteristic> characteristicOptional = characteristicRepository.findById(characteristicDto.getId());

                if (characteristicOptional.isPresent()) {
                    Characteristic characteristicExisting = characteristicOptional.get();
                    newCharacteristics.add(characteristicExisting);
                } else {
                    String errorMessage = "Característica con ID " + characteristicDto.getId() + " no encontrada.";
                    logger.error(errorMessage);
                    throw new NonExistentCharacteristicException("No se encontró la característica con ID: " + characteristicDto.getId());
                }
            } catch (Exception e) {
                String errorMessage = "Error al procesar característica: " + e.getMessage();
                logger.error(errorMessage, e);
            }
        }

        try {
            if (instrument.getCharacteristics() != null){
                instrument.getCharacteristics().clear();
                instrument.getCharacteristics().addAll(newCharacteristics);
            }
            else {
                instrument.setCharacteristics(newCharacteristics);
            }

            instrumentRepository.save(instrument);
        } catch (Exception e) {
            String errorMessage = "Error al asociar las características al instrumento con ID: " + e.getMessage();
            logger.error(errorMessage);
        }
    }

    @Transactional
    public void deleteCharacteristic(Long id) {
        try{
            Optional<Characteristic> characteristicOptional = characteristicRepository.findById(id);
            characteristicOptional.ifPresent(characteristic -> {
                this.removeCharacteristicAndSave(characteristic.getId());
                characteristic.setDeleted(true);
                this.characteristicRepository.save(characteristic);
            });
            if (characteristicOptional.isEmpty()) {
                throw new NonExistentCharacteristicException("No se encontró la característica con ID: " + id);
            }
        } catch(Exception e){
            throw new DeleteCharacteristicException("Error al eliminar la característica con ID: "+id);
        }
    }

    @Transactional
    public void removeCharacteristicAndSave(Long characteristicId) {
        List<Instrument> instruments = instrumentRepository.findByCharacteristicsIdAndDeletedIsFalse(characteristicId);

        for (Instrument instrument : instruments) {
            instrument.getCharacteristics().removeIf(characteristic -> characteristic.getId().equals(characteristicId));
        }

        instrumentRepository.saveAll(instruments);
    }
}