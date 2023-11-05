package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CaracteristicaDto;
import com.proyecto.integrador.dto.InstrumentoDto;
import com.proyecto.integrador.entity.Caracteristica;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.*;
import com.proyecto.integrador.repository.CaracteristicaRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CaracteristicaService {

    private static final Logger logger = LogManager.getLogger(CaracteristicaService.class);
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;
    @Autowired
    private InstrumentoRepository instrumentoRepository;


    @Transactional
    public Caracteristica crearCaracteristica(CaracteristicaDto caracteristicaDto) {
    try {
        logger.info("Se va a crear la característica con nombre: {} y icono: {}", caracteristicaDto.getNombre(), caracteristicaDto.getIcono());


        if (caracteristicaRepository.findByNombre(caracteristicaDto.getNombre()).isPresent()) {
            throw new DuplicateCategoriaException("Ya existe una categoría con el mismo nombre: " + caracteristicaDto.getNombre());
        }

        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setNombre(caracteristicaDto.getNombre());
        caracteristica.setIcono(caracteristicaDto.getIcono());


        Caracteristica nuevaCaracteristica = caracteristicaRepository.save(caracteristica);
        logger.info("La característica con ID {} ha sido creada exitosamente.", nuevaCaracteristica.getId());

        return nuevaCaracteristica;
        } catch (Exception e){
        throw new CreateCharacteristicException("Ocurrió un error al crear la característica", e);
    }
    }

    @Transactional
    public Caracteristica editarCaracteristica(Long id, CaracteristicaDto caracteristicaDto) {
        try {
            Optional<Caracteristica> caracteristicasOptional = caracteristicaRepository.findById(id);
            if (caracteristicasOptional.isPresent()) {
                Caracteristica caracteristica = caracteristicasOptional.get();
                logger.info("Se va a modificar la característica con ID: {}, atributo nombre: {}, icono: {}",
                        id, caracteristica.getNombre(), caracteristica.getIcono());

                if (existeOtraCaracteristicaConMismoNombre(id, caracteristicaDto.getNombre())) {
                    throw new DuplicateCaracteristicaException("Ya existe otra característica con el mismo nombre: " + caracteristicaDto.getNombre());
                }

                caracteristica.setNombre(caracteristicaDto.getNombre());
                caracteristica.setIcono(caracteristicaDto.getIcono());


                caracteristicaRepository.save(caracteristica);
                logger.info("La característica con ID: {} fue modificada correctamente, atributo nombre: {}, icono: {}",
                        caracteristica.getId(), caracteristica.getNombre(), caracteristica.getIcono());

                return caracteristica;
            } else {
                throw new EntityNotFoundException("No se encontró la característica con ID: " + id);
            }
        } catch (Exception e) {
            throw new EditCharacteristException("Ocurrió un error al editar la característica", e);
        }
    }


    @Transactional
    public void eliminarCaracteristica(Long id) {
        try{
            Optional<Caracteristica> caracteristicaOptional = caracteristicaRepository.findById(id);
            caracteristicaOptional.ifPresent(caracteristica -> {
                caracteristica.setEliminada(true);
                this.caracteristicaRepository.save(caracteristica);
            });
            if (caracteristicaOptional.isEmpty()) {
                throw new NonExistentCharacteristicExcpetion("No se encontró la característica con ID: " + id);
            }
        } catch(Exception e){
            throw new DeletedCharacteristException("Error al eliminar la característica con ID: "+id);
        }
    }


    // obtener la lista de características
    public List<Caracteristica> listarCaracteristica() {
        logger.info("Obteniendo la lista de características registradas.");
        List<Caracteristica> caracteristica = caracteristicaRepository.findAll();
        logger.info("Lista de características obtenida exitosamente. Cantidad de características: {}", caracteristica.size());
        return caracteristica;
    }



    // metodo de validacion utilizado en editar caracteristica.
    private boolean existeOtraCaracteristicaConMismoNombre(Long id, String nombre) {
        Optional<Caracteristica> existeCaracteristica = caracteristicaRepository.findByNombre(nombre);

        if (existeCaracteristica.isPresent()) {
            return !existeCaracteristica.get().getId().equals(id);
        }
        return false;
    }

    public void asociarCaracteristica(Instrumento instrumento, List<CaracteristicaDto> caracteristicasDto) {
        List<Caracteristica> nuevasCaracteristicas = new ArrayList();

        for (CaracteristicaDto caracteristicaDto : caracteristicasDto) {
            try {
                Optional<Caracteristica> caracteristicaOptional = caracteristicaRepository.findById(caracteristicaDto.getId());

                if (caracteristicaOptional.isPresent()) {
                    Caracteristica caracteristicaExistente = caracteristicaOptional.get();
                    nuevasCaracteristicas.add(caracteristicaExistente);
                } else {
                    System.out.println("Característica con ID " + caracteristicaDto.getId() + " no encontrada.");
                    throw new NonExistentCharacteristicExcpetion("No se encontró la característica con ID: " + caracteristicaDto.getId());
                }
            } catch (Exception e) {
                e.printStackTrace(); //Agregar excepcion personalizada
            }
        }

        try {
            instrumento.getCaracteristicas().clear();
            instrumento.getCaracteristicas().addAll(nuevasCaracteristicas);
            instrumentoRepository.save(instrumento);
        } catch (Exception e) {
            e.printStackTrace(); //Agregar excepcion personalizada
        }
    }





}

