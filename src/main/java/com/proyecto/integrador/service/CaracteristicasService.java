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

import java.util.Optional;

@Service
public class CaracteristicasService {

    @Autowired
    private CaracteristicasRepository caracteristicasRepository;
    @Autowired
    private InstrumentoRepository instrumentoRepository;


    @Transactional
    public Caracteristicas crearCaracteristica(CaracteristicasDto caracteristicasDto) {

        Caracteristicas caracteristicas = new Caracteristicas();
        caracteristicas.setNombre(caracteristicasDto.getNombre());
        caracteristicas.setIcono(caracteristicasDto.getIcono());

        Optional<Instrumento> instrumentoOptional = instrumentoRepository.findById(caracteristicasDto.getInstrumentoId());
        Instrumento instrumento = instrumentoOptional.orElseThrow(() ->
                new EntityNotFoundException("No se encontró el instrumento con el ID: " + caracteristicasDto.getInstrumentoId()));

        caracteristicas.setInstrumento(instrumento);

        return caracteristicasRepository.save(caracteristicas);
    }

    @Transactional
    public Caracteristicas editarCaracteristica(Long id, CaracteristicasDto caracteristicasDto) {
        Optional<Caracteristicas> caracteristicasOptional = caracteristicasRepository.findById(id);
        if (caracteristicasOptional.isPresent()) {
            Caracteristicas caracteristicas = caracteristicasOptional.get();
            caracteristicas.setNombre(caracteristicasDto.getNombre());
            caracteristicas.setIcono(caracteristicasDto.getIcono());

            Optional<Instrumento> instrumentoOptional = instrumentoRepository.findById(caracteristicasDto.getInstrumentoId());
            Instrumento instrumento = instrumentoOptional.orElseThrow(() ->
                    new EntityNotFoundException("No se encontró el instrumento con el ID: " + caracteristicasDto.getInstrumentoId()));

            caracteristicas.setInstrumento(instrumento);

            return caracteristicasRepository.save(caracteristicas);
        } else {
            throw new EntityNotFoundException("No se encontró la característica con ID: " + id);
        }
    }


        @Transactional
        public void eliminarCaracteristica (Long id){
            Caracteristicas caracteristicas = caracteristicasRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la característica con el ID: " + id));

            caracteristicasRepository.delete(caracteristicas);
        }
}

