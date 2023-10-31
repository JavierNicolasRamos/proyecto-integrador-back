package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.CaracteristicasDto;
import com.proyecto.integrador.entity.Caracteristicas;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.repository.CaracteristicasRepository;
import com.proyecto.integrador.repository.InstrumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Optional;

@Service
public class CaracteristicasService {

    private static final Logger logger = LogManager.getLogger(CaracteristicasService.class);
    @Autowired
    private CaracteristicasRepository caracteristicasRepository;
    @Autowired
    private InstrumentoRepository instrumentoRepository;


    @Transactional
    public Caracteristicas crearCaracteristica(CaracteristicasDto caracteristicasDto) {

        logger.info("Se va a crear la característica con nombre: {} y icono: {}", caracteristicasDto.getNombre(), caracteristicasDto.getIcono());

        Caracteristicas caracteristicas = new Caracteristicas();
        caracteristicas.setNombre(caracteristicasDto.getNombre());
        caracteristicas.setIcono(caracteristicasDto.getIcono());

        Optional<Instrumento> instrumentoOptional = instrumentoRepository.findById(caracteristicasDto.getInstrumentoId());
        Instrumento instrumento = instrumentoOptional.orElseThrow(() ->
                new EntityNotFoundException("No se encontró el instrumento con el ID: " + caracteristicasDto.getInstrumentoId()));

        caracteristicas.setInstrumento(instrumento);

        Caracteristicas nuevaCaracteristica = caracteristicasRepository.save(caracteristicas);
        logger.info("La característica con ID {} ha sido creada exitosamente.", nuevaCaracteristica.getId());

        return nuevaCaracteristica;
    }

    @Transactional
    public Caracteristicas editarCaracteristica(Long id, CaracteristicasDto caracteristicasDto) {
        Optional<Caracteristicas> caracteristicasOptional = caracteristicasRepository.findById(id);
        if (caracteristicasOptional.isPresent()) {
            Caracteristicas caracteristicas = caracteristicasOptional.get();
            logger.info("Se va a modificar la característica con ID: {}, atributo nombre: {}, icono: {}",
                    id, caracteristicas.getNombre(), caracteristicas.getIcono());
            caracteristicas.setNombre(caracteristicasDto.getNombre());
            caracteristicas.setIcono(caracteristicasDto.getIcono());

            Optional<Instrumento> instrumentoOptional = instrumentoRepository.findById(caracteristicasDto.getInstrumentoId());
            Instrumento instrumento = instrumentoOptional.orElseThrow(() ->
                    new EntityNotFoundException("No se encontró el instrumento con el ID: " + caracteristicasDto.getInstrumentoId()));

            caracteristicas.setInstrumento(instrumento);

            Caracteristicas caracteristicaEditada = caracteristicasRepository.save(caracteristicas);
            logger.info("La característica con ID: {} fue modificada correctamente, atributo nombre: {}, icono: {}",
                    caracteristicaEditada.getId(), caracteristicaEditada.getNombre(), caracteristicaEditada.getIcono());

            return caracteristicaEditada;
        } else {
            throw new EntityNotFoundException("No se encontró la característica con ID: " + id);
        }
    }


        @Transactional
        public void eliminarCaracteristica (Long id){
        try {
            Caracteristicas caracteristicas = caracteristicasRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la característica con el ID: " + id));
            logger.info("Se va a eliminar la característica con ID: {}, nombre: {}, icono: {}",
                    caracteristicas.getId(), caracteristicas.getNombre(), caracteristicas.getIcono());
            caracteristicasRepository.delete(caracteristicas);
            logger.info("La característica con ID: {} fue eliminada correctamente", id);
        } catch(Exception e){
            logger.error("Se produjo un error al eliminar la característica con ID: " + id + ". Error: " + e.getMessage());
            throw e;
        }
    }
}

