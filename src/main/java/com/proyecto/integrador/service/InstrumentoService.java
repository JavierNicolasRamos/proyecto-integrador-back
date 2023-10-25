package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.InstrumentoDto;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.exception.DuplicateInstrumentException;
import com.proyecto.integrador.exception.EliminacionInstrumentoException;
import com.proyecto.integrador.exception.InstrumentoGetAllException;
import com.proyecto.integrador.exception.NonExistentInstrumentException;
import com.proyecto.integrador.repository.InstrumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.webjars.NotFoundException;

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
        instrumento.setPuntuacion(instrumentoDto.getPuntuacion());
        instrumento.setDetalle(instrumentoDto.getDetalle());
        instrumento.setDisponible(true);

        instrumentoRepository.save(instrumento);
        this.imagenService.guardarImagenesInstrumento(instrumento);

        return instrumento;
    }

    public Page<Instrumento> obtenerDiezInstrumentos(Pageable pageable) {
        try {
            return instrumentoRepository.findRandomInstruments(pageable);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("No se encontraron instrumentos aleatorios.");
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener instrumentos aleatorios.", ex);
        }
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
            instrumento.setPuntuacion(instrumentoDto.getPuntuacion());
            instrumento.setDetalle(instrumentoDto.getDetalle());
            instrumento.setDisponible(instrumentoDto.getDisponible());

            this.imagenService.actualizarImagenesInstrumento(instrumento);

            return instrumentoRepository.save(instrumento);
        } else {
            throw new NonExistentInstrumentException("No se encontró el instrumento con ID: " + id);
        }
    }

    public void eliminarInstrumento(Long id) {
        try {
            instrumentoRepository.deleteById(id); //TODO: hacer un update para la columna eliminado (true/false)
        } catch (Exception e) {
            throw new EliminacionInstrumentoException("Error al eliminar el instrumento con ID: " + id);
        }
    }

    public Page<Instrumento> getAll(Pageable pageable) {
        try {
            return instrumentoRepository.getAll(pageable);
        } catch (Exception e) {
            throw new InstrumentoGetAllException("Error al recuperar la lista de instrumentos.", e);
        }
    }
}