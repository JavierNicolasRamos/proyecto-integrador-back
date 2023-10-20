package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.InstrumentoDto;
import com.proyecto.integrador.entity.Imagen;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.DuplicateInstrumentException;
import com.proyecto.integrador.exception.EliminacionInstrumentoException;
import com.proyecto.integrador.exception.NonExistentInstrumentException;
import com.proyecto.integrador.repository.InstrumentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class InstrumentoService {
    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    public InstrumentoService(InstrumentoRepository instrumentoRepository) {
        this.instrumentoRepository = instrumentoRepository;
    }

    @Transactional
    public Instrumento crearInstrumento(InstrumentoDto instrumentoDto) {
        Optional<Instrumento> existeInstrumento = instrumentoRepository.getByNombre(instrumentoDto.getNombre());
        if (existeInstrumento.isPresent()){
            throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentoDto.getNombre());
        }

        Instrumento instrumento = new Instrumento();
        instrumento.setNombre(instrumentoDto.getNombre());
        instrumento.setCategoria(instrumentoDto.getCategoria());
        instrumento.setFechaCarga(LocalDate.now());
        instrumento.setFechaUpdate(LocalDate.now());
        instrumento.setDisponible(true);

        instrumentoRepository.save(instrumento);
        this.imagenService.guardarImagenesInstrumento(instrumento);

        return instrumento;
    }

    public List<Instrumento> obtenerDiezInstrumentos() {
        List<Instrumento> todosLosInstrumentos = instrumentoRepository.findAll();
        int totalInstrumentos = todosLosInstrumentos.size();
        int numeroInstrumentosAObtener = Math.min(10, totalInstrumentos);

        if (numeroInstrumentosAObtener == 0) {
            return Collections.emptyList();
        }

        Random random = new Random();
        List<Instrumento> instrumentosAleatorios = random.ints(0, totalInstrumentos)
                .distinct()
                .limit(numeroInstrumentosAObtener)
                .mapToObj(todosLosInstrumentos::get)
                .collect(Collectors.toList());

        return instrumentosAleatorios;
    }

    public Instrumento obtenerInstrumentoPorId(Long id) {
        Optional<Instrumento> instrumento = instrumentoRepository.findById(id);
        return instrumento.orElse(null);
    }

    @Transactional
    public Instrumento actualizarInstrumento(Long id, InstrumentoDto instrumentoDto) {
        Optional<Instrumento> existente = instrumentoRepository.findById(id);

        if (existente.isPresent()) {
            Instrumento instrumento = existente.get();

            if (!instrumento.getNombre().equals(instrumentoDto.getNombre())) {
                Optional<Instrumento> otroConMismoNombre = instrumentoRepository.getByNombre(instrumentoDto.getNombre());
                if (otroConMismoNombre.isPresent()) {
                    throw new DuplicateInstrumentException("Ya existe un instrumento con el mismo nombre: " + instrumentoDto.getNombre());
                }
            }

            instrumento.setNombre(instrumentoDto.getNombre());
            instrumento.setCategoria(instrumentoDto.getCategoria());
            instrumento.setFechaUpdate(LocalDate.now());
            instrumento.setDisponible(instrumentoDto.getDisponible());

            this.imagenService.actualizarImagenesInstrumento(instrumento);

            return instrumentoRepository.save(instrumento);
        } else {
            throw new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id);
        }
    }

    public void eliminarInstrumento(Long id) {
        try {
            instrumentoRepository.deleteById(id);
        } catch (Exception e) {
            throw new EliminacionInstrumentoException("Error al eliminar el instrumento con ID: " + id);
        }
    }
}