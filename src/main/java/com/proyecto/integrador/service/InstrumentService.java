package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Category;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.exception.DuplicateInstrumentException;
import com.proyecto.integrador.exception.DeleteInstrumentException;
import com.proyecto.integrador.exception.InstrumentGetAllException;
import com.proyecto.integrador.exception.NonExistentInstrumentException;
import com.proyecto.integrador.repository.CharacteristicRepository;
import com.proyecto.integrador.repository.InstrumentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.webjars.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InstrumentService {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentService.class);

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private CharacteristicRepository characteristicRepository;

    @Autowired
    private CharacteristicService characteristicService;

    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;
    
    @Transactional
    public Instrument createInstrument(InstrumentDto instrumentDto) {
        logger.info("Iniciando el proceso de creación de instrumento...");

        try {
            Optional<Instrument> instrumentExists = instrumentRepository.getByName(instrumentDto.getName());
            if (instrumentExists.isPresent()) {
                logger.error("Ya existe un instrumento con el mismo nombre: "+ instrumentDto.getName());
                throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentDto.getName());
            }

            Instrument instrument = new Instrument();
            instrument.setName(instrumentDto.getName());

//ARMANDO EL DTO CON LO QUE RECIBO
//            Category category = new Category();//Lo hago para que sea más legible el código
//            CategoryDto categoryDto = instrumentDto.getCategoryDto();
//
//            //Category
//            category.setId(categoryDto.getId());
//            category.setName(categoryDto.getName());
//            category.setDetails(categoryDto.getDetails());
//
//
//
//            //Image
//            Image image = new Image();//Lo hago para que sea más legible el código
//            ImageDto imageDto = categoryDto.getImageDto();
//
//
//            image.setId(image.getId());
//            image.setImage(image.getImage());
//            image.setDeleted(false);
//
//            category.setImage(image);
//            category.setDeleted(false);
            //Category category = categoryService.categoryById(instrument.getCategory().getId());
            Category category = categoryService.categoryByName(instrumentDto.getCategoryDto().getName());
            instrument.setCategory(category);
            instrument.setUploadDate(LocalDate.now());
            instrument.setUpdateDate(LocalDate.now());
            instrument.setScore(instrumentDto.getScore());
            instrument.setDetail(instrumentDto.getDetail());
            instrument.setAvailable(true);
            logger.info("Se va a crear el instrumento con nombre: " + instrumentDto.getName());
            instrumentRepository.save(instrument);

            if(!instrumentDto.getImage().isEmpty()){
                this.imageService.saveImagesInstrument(instrument, instrumentDto.getImage());
            }

            if(!instrumentDto.getCharacteristics().isEmpty()){
                this.characteristicService.associateCharacteristic(instrument, instrumentDto.getCharacteristics());
            }

            logger.info("Instrumento creado con éxito, nombre: " + instrument.getName());
            return instrument;
        } catch(DuplicateInstrumentException e){
            logger.error("Error al crear el instrumento: " + e.getMessage());
            throw e; //TODO: sumar la excepcion customizada
        }
         catch(Exception e){
             logger.error("Error inesperado al crear el instrumento: " + e.getMessage(), e);
             throw e;
        }
    }

    public List<Instrument> getTenInstruments() {
        try {
            //TODO: Establece el tamaño de página deseado en 10
            return instrumentRepository.findRandomInstruments();
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("No se encontraron instrumentos aleatorios.");
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener instrumentos aleatorios.", e);
        }
    }

    public Instrument getInstrumentById(Long id) {
        logger.info("Iniciando la obtención del instrumento con ID: " + id);
        try {
            Instrument instrument = instrumentRepository.findById(id).orElseThrow(()
                    -> new EntityNotFoundException("No se encontró el instrumento"));
            logger.info("Instrumento con ID " + id + " obtenido con éxito.");
            return instrument;
        } catch (EntityNotFoundException e) {
            logger.error("Error al obtener el instrumento con ID " + id + ": " + e.getMessage());
            throw e; //TODO: sumar la excepcion customizada
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el instrumento con ID " + id + ": " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Instrument updateInstrument(Long id, InstrumentDto instrumentDto) {
        logger.info("Iniciando la actualización del instrumento con ID: " + id);
        Optional<Instrument> existing = instrumentRepository.findById(id);

       try {
           if (existing.isPresent()) {
               Instrument instrument = existing.get();

               if (!instrument.getName().equals(instrumentDto.getName())) {
                   Optional<Instrument> anotherWithTheSameName = instrumentRepository.getByName(instrumentDto.getName());
                   if (anotherWithTheSameName.isPresent()) {
                       logger.error("Ya existe un instrumento con el mismo nombre: "+ instrumentDto.getName());
                       throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentDto.getName());
                   }
               }

               instrument.setName(instrumentDto.getName());
               instrument.setCategory(categoryService.categoryByName(instrumentDto.getCategoryDto().getName()));//Busca la categoría de la DB y la trae
               instrument.setUpdateDate(LocalDate.now());
               instrument.setScore(instrumentDto.getScore());
               instrument.setDetail(instrumentDto.getDetail());
               instrument.setAvailable(instrumentDto.getAvailable());

               if(!instrumentDto.getImage().isEmpty()){
                   this.imageService.updateImagesInstrument(instrument, instrumentDto.getImage());
               }

               if(!instrumentDto.getCharacteristics().isEmpty()){
                   this.characteristicService.associateCharacteristic(instrument, instrumentDto.getCharacteristics());
               }

               logger.info("Instrumento con ID " + id + " actualizado con éxito.");
               return instrumentRepository.save(instrument);
           } else {
               throw new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id);
           }
       }catch(DuplicateInstrumentException | NonExistentInstrumentException e) {
           logger.error("Error al actualizar el instrumento: " + e.getMessage());
           throw e; //TODO: sumar la excepcion customizada
       }
       catch (Exception e) {
           logger.error("Error inesperado al actualizar el instrumento: " + e.getMessage(), e);
           throw e;
       }
    }

    public void deleteInstrument(Long id) {
        try {
            Optional<Instrument> instrumentOptional  = instrumentRepository.findById(id);
            instrumentOptional.ifPresent(instrument ->{
                instrument.setDeleted(true);
                this.instrumentRepository.save(instrument);
            });
            if (instrumentOptional.isEmpty()) {
                throw new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id);
            }
        } catch (Exception e) {
            throw new DeleteInstrumentException("Error al eliminar el instrumento con ID: " + id);
        }
    }

    public Page<Instrument> getAll(Pageable pageable) {
        logger.info("Iniciando la obtención de la lista de instrumentos...");
        try {
            logger.info("Obtención de la lista de instrumentos completada con éxito.");
            return instrumentRepository.getAll(pageable);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de instrumentos");
            throw new InstrumentGetAllException("Error al recuperar la lista de instrumentos.", e);
        }
    }

    public Page<Instrument> getName(String name, Pageable pageable){
        try {
            return this.instrumentRepository.getName(name, pageable);
        }
        catch (Exception e){
            logger.error("Error al obtener la lista de instrumentos");
            throw new InstrumentGetAllException("Error al recuperar la lista de instrumentos.", e);
        }
    }
}