package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.InstrumentoDto;
import com.proyecto.integrador.entity.Caracteristica;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.DuplicateInstrumentException;
import com.proyecto.integrador.exception.EliminacionInstrumentoException;
import com.proyecto.integrador.exception.InstrumentoGetAllException;
import com.proyecto.integrador.exception.NonExistentInstrumentException;
import com.proyecto.integrador.repository.CaracteristicaRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
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
public class InstrumentoService {

    private static final Logger logger = LoggerFactory.getLogger(InstrumentoService.class);

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private CaracteristicaService caracteristicaService;

    @Autowired
    private ImagenService imagenService;
    
    @Transactional
    public Instrumento crearInstrumento(InstrumentoDto instrumentoDto) {
        logger.info("Iniciando el proceso de creación de instrumento...");

        try {
            Optional<Instrumento> existeInstrumento = instrumentoRepository.getByNombre(instrumentoDto.getNombre());
            if (existeInstrumento.isPresent()) {
                logger.error("Ya existe un instrumento con el mismo nombre: "+ instrumentoDto.getNombre());
                throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentoDto.getNombre());
            }

            Instrumento instrumento = new Instrumento();
            instrumento.setNombre(instrumentoDto.getNombre());
            instrumento.setCategoria(instrumentoDto.getCategoria());
            instrumento.setFechaCarga(LocalDate.now());
            instrumento.setFechaUpdate(LocalDate.now());
            instrumento.setPuntuacion(instrumentoDto.getPuntuacion());
            instrumento.setDetalle(instrumentoDto.getDetalle());
            instrumento.setDisponible(true);
            logger.info("Se va a crear el instrumento con nombre: " + instrumentoDto.getNombre());
            instrumentoRepository.save(instrumento);
          
            this.imagenService.guardarImagenesInstrumento(instrumento,instrumentoDto.getImagen());
            this.caracteristicaService.asociarCaracteristica(instrumento, instrumentoDto.getCaracteristicas());
         
            logger.info("Instrumento creado con éxito, nombre: " + instrumento.getNombre());
            return instrumento;
        } catch(DuplicateInstrumentException e){
            logger.error("Error al crear el instrumento: " + e.getMessage());
            throw e;
        }
         catch(Exception e){
             logger.error("Error inesperado al crear el instrumento: " + e.getMessage(), e);
             throw e;
        }
    }

    public List<Instrumento> obtenerDiezInstrumentos() {
        try {
            // Establece el tamaño de página deseado en 10
            return instrumentoRepository.findRandomInstruments();
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("No se encontraron instrumentos aleatorios.");
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener instrumentos aleatorios.", e);
        }
    }

    public Instrumento obtenerInstrumentoPorId(Long id) {
        logger.info("Iniciando la obtención del instrumento con ID: " + id);
        try {
            Instrumento instrumento = instrumentoRepository.buscarPorId(id).orElseThrow(()
                    -> new EntityNotFoundException("No se encontró el instrumento"));
            logger.info("Instrumento con ID " + id + " obtenido con éxito.");
            return instrumento;
        } catch (EntityNotFoundException e) {
            logger.error("Error al obtener el instrumento con ID " + id + ": " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el instrumento con ID " + id + ": " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public Instrumento actualizarInstrumento(Long id, InstrumentoDto instrumentoDto) {
        logger.info("Iniciando la actualización del instrumento con ID: " + id);
        Optional<Instrumento> existente = instrumentoRepository.findById(id);

       try {
           if (existente.isPresent()) {
               Instrumento instrumento = existente.get();

               if (!instrumento.getNombre().equals(instrumentoDto.getNombre())) {
                   Optional<Instrumento> otroConMismoNombre = instrumentoRepository.getByNombre(instrumentoDto.getNombre());
                   if (otroConMismoNombre.isPresent()) {
                       logger.error("Ya existe un instrumento con el mismo nombre: "+ instrumentoDto.getNombre());
                       throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentoDto.getNombre());
                   }
               }

               instrumento.setNombre(instrumentoDto.getNombre());
               instrumento.setCategoria(instrumentoDto.getCategoria());
               instrumento.setFechaUpdate(LocalDate.now());
               instrumento.setPuntuacion(instrumentoDto.getPuntuacion());
               instrumento.setDetalle(instrumentoDto.getDetalle());
               instrumento.setDisponible(instrumentoDto.getDisponible());
             
               this.imagenService.actualizarImagenesInstrumento(instrumento, instrumentoDto.getImagen());
               this.caracteristicaService.asociarCaracteristica(instrumento, instrumentoDto.getCaracteristicas());
             
               logger.info("Instrumento con ID " + id + " actualizado con éxito.");
               return instrumentoRepository.save(instrumento);
           } else {
               throw new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id);
           }
       }catch(DuplicateInstrumentException | NonExistentInstrumentException e) {
           logger.error("Error al actualizar el instrumento: " + e.getMessage());
           throw e;
       }
       catch (Exception e) {
           logger.error("Error inesperado al actualizar el instrumento: " + e.getMessage(), e);
           throw e;
       }
    }


    public void eliminarInstrumento(Long id) {
        try {
            Optional<Instrumento> instrumentoOptional  = instrumentoRepository.findById(id);
            instrumentoOptional.ifPresent(instrumento ->{
                instrumento.setEliminado(true);
                this.instrumentoRepository.save(instrumento);
            });
            if (instrumentoOptional.isEmpty()) {
                throw new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id);
            }
        } catch (Exception e) {
            throw new EliminacionInstrumentoException("Error al eliminar el instrumento con ID: " + id);
        }
    }

    public Page<Instrumento> getAll(Pageable pageable) {
        logger.info("Iniciando la obtención de la lista de instrumentos...");
        try {
            logger.info("Obtención de la lista de instrumentos completada con éxito.");
            return instrumentoRepository.getAll(pageable);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de instrumentos: ");
            throw new InstrumentoGetAllException("Error al recuperar la lista de instrumentos.", e);
        }
    }

    public Page<Instrumento>getNombre(String nombre, Pageable pageable){
        try {
            return this.instrumentoRepository.getNombre(nombre, pageable);
        }
        catch (Exception e){
            throw new InstrumentoGetAllException("Error al recuperar la lista de instrumentos.", e);
        }
    }




}