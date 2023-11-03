package com.proyecto.integrador.service;

import com.amazonaws.services.dynamodbv2.document.Page;
import com.proyecto.integrador.dto.CaracteristicaDto;
import com.proyecto.integrador.entity.Caracteristica;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.DuplicateCaracteristicaException;
import com.proyecto.integrador.exception.DuplicateCategoriaException;
import com.proyecto.integrador.repository.CaracteristicaRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.print.Pageable;
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
    }

    @Transactional
    public Caracteristica editarCaracteristica(Long id, CaracteristicaDto caracteristicaDto) {
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
    }


        @Transactional
        public void eliminarCaracteristica (Long id){
        try {
            Caracteristica caracteristica = caracteristicaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la característica con el ID: " + id));
            logger.info("Se va a eliminar la característica con ID: {}, nombre: {}, icono: {}",
                    caracteristica.getId(), caracteristica.getNombre(), caracteristica.getIcono());
            caracteristicaRepository.delete(caracteristica);
            logger.info("La característica con ID: {} fue eliminada correctamente", id);
        } catch(Exception e){
            logger.error("Se produjo un error al eliminar la característica con ID: " + id + ". Error: " + e.getMessage());
            throw e;
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


    //TODO: metodo para asociar y quitar caracteristicas, hecho en instrumentoService.

}

