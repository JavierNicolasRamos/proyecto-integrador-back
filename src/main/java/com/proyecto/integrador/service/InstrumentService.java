package com.proyecto.integrador.service;

import com.proyecto.integrador.commons.userValidation;
import com.proyecto.integrador.dto.InstrumentDto;
import com.proyecto.integrador.entity.Image;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.exception.*;
import com.proyecto.integrador.repository.InstrumentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivilegedAction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InstrumentService {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentService.class.getName());

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private CharacteristicService characteristicService;

    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private userValidation userValidation;

    @Transactional
    public Instrument createInstrument(InstrumentDto instrumentDto, List<MultipartFile> multipartFiles) {
        logger.info("Iniciando el proceso de creación de instrumento...");

        try {
            Optional<Instrument> instrumentExists = instrumentRepository.getByName(instrumentDto.getName());
            if (instrumentExists.isPresent()) {
                logger.error("Ya existe un instrumento con el mismo nombre: " + instrumentDto.getName());
                throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentDto.getName());
            }

            Instrument instrument = new Instrument();
            instrument.setName(instrumentDto.getName());
            instrument.setCategory(this.categoryService.categoryById(instrumentDto.getCategoryDto().getId()));
            instrument.setUploadDate(LocalDate.now());
            instrument.setUpdateDate(LocalDate.now());
            instrument.setScore(instrumentDto.getScore());
            instrument.setDetail(instrumentDto.getDetail());
            instrument.setAvailable(true);
            instrument.setDeleted(false);
            instrument.setImage(this.imageService.createAllImages(multipartFiles));
            instrument.setSeller(userService.findByEmail(instrumentDto.getSellerDto().getEmail()));

            instrumentRepository.save(instrument);

            if (!instrumentDto.getCharacteristics().isEmpty()) {
                this.characteristicService.associateCharacteristic(instrument, instrumentDto.getCharacteristics());
            }

            logger.info("Instrumento creado con éxito, nombre: " + instrument.getName());
            return instrument;
        } catch (DuplicateInstrumentException e) {
            logger.error("Error al crear el instrumento: " + e.getMessage());
            throw e; //TODO: sumar la excepcion customizada
        } catch (Exception e) {
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
                        logger.error("Ya existe un instrumento con el mismo nombre: " + instrumentDto.getName());
                        throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentDto.getName());
                    }
                }

                instrument.setName(instrumentDto.getName());
                instrument.setCategory(this.categoryService.categoryById(instrumentDto.getCategoryDto().getId()));
                instrument.setUpdateDate(LocalDate.now());
                instrument.setScore(instrumentDto.getScore());
                instrument.setDetail(instrumentDto.getDetail());
                instrument.setAvailable(instrumentDto.getAvailable());

                if (!instrumentDto.getCharacteristics().isEmpty()) {
                    this.characteristicService.associateCharacteristic(instrument, instrumentDto.getCharacteristics());
                }

                logger.info("Instrumento con ID " + id + " actualizado con éxito.");
                return instrumentRepository.save(instrument);
            } else {
                throw new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id);
            }
        } catch (DuplicateInstrumentException | NonExistentInstrumentException e) {
            logger.error("Error al actualizar el instrumento: " + e.getMessage());
            throw e; //TODO: sumar la excepcion customizada
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar el instrumento: " + e.getMessage(), e);
            throw e;
        }
    }

    public void deleteInstrument(Long id) {
        try {
            Optional<Instrument> instrumentOptional = instrumentRepository.findById(id);
            instrumentOptional.ifPresent(instrument -> {
                instrument.getImage().forEach(image -> this.imageService.deleteImage(image.getId()));
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

    public Page<Instrument> getName(String name, Pageable pageable) {

        try {
            return this.instrumentRepository.getName(name, pageable);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de instrumentos");
            throw new InstrumentGetAllException("Error al recuperar la lista de instrumentos.", e);
        }
    }

    public Instrument createImagesInstrument(Long id, List<MultipartFile> images) {
        try {
            Instrument instrument = instrumentRepository.findById(id)
                    .orElseThrow(() -> new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id));

            List<Image> newImages = imageService.createAllImages(images);
            instrument.getImage().addAll(newImages);

            return instrumentRepository.save(instrument);
        } catch (Exception e) {
            throw new InstrumentImageCreationException("Error al crear imágenes para el instrumento con ID: " + id, e);
        }
    }

    @Transactional
    public void updateAvgScore(Double reviewScore, Long idInstrument) {
        logger.info("Inciando actualización de puntaje promedio y cantidad de calificaciones...");
        try {
            Instrument instrument = this.getInstrumentById(idInstrument);

            Long reviewCount = instrument.getReviewCount();
            Double reviewAverage = instrument.getScore();

            Double newReviewAvgScore = ((reviewCount * reviewAverage) + reviewScore) / (reviewCount + 1);

            instrument.setReviewCount(reviewCount + 1);
            instrument.setScore(newReviewAvgScore);

            instrumentRepository.save(instrument);
            logger.info("Calificación y Cantidad de reseñas actualizado correctamente");
        } catch (InstrumentUpdateAvgScoreException e) {
            logger.error("Error al intentar actualizar puntaje promedio y cantidad de calificaciones");
            throw new InstrumentUpdateAvgScoreException("No se pudo actualizar el puntaje promedio del instrumento con el ID:");
        }
    }

//    public List<Instrument> getFavouritesByMail(String email){
//        logger.info("Iniciando la recuperación de los instrumentos favoritos para el usuario con mail: " + email);
//        try{
//            List<User> userList = new ArrayList<User>();
//            userList.add(userService.findByEmail(email));
//            userService.findByEmail(email);
//            Optional<List<Instrument>> favouriteInstruments = instrumentRepository.findAllByUserAndDeleted(userService.findByEmail(email), false );
//            logger.info("Lista de favoritos retornada con éxito");
//            return favouriteInstruments.get();
//        }catch(InstrumentGetFavouritesByEmailException e){
//            logger.error("No se pudo recuperar la lista de favoritos de usuario con email: " + email);
//            throw new InstrumentGetFavouritesByEmailException("No se pudieron recuperar los favoritos");
//        }
//    }
//
//    public Instrument addFavourite(String email, InstrumentDto instrumentDto){
//        logger.info("Iniciando el agregado de un favorit...");
//        try{
//            User user = userService.findByEmail(email);
//            String message = "No se puede agregar a favoritos un producto propio";
//            Instrument instrument = this.getInstrumentById(instrumentDto.getId());
//            userValidation.userValidation(email,instrument.getSeller().getEmail() , message );
//
//            List<User> usersList = instrument.getUsers();
//            usersList.add(user);
//            instrument.setUsers(usersList);
//            logger.info("Instrumento agregado a favoritos correctamente");
//            return instrument;
//        }catch (InstrumentAddFavouriteException e){
//            logger.error("Error al intenatar agregar el instrumento con ID:" + instrumentDto.getId() + " a favoritos");
//            throw new InstrumentAddFavouriteException("No se pudo agregar el intrumento con ID: " + instrumentDto.getId() + " a favoritos");
//        }
//    }
}