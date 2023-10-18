package com.proyecto.integrador.service;

import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.repository.InstrumentoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrumentoService {
    private final InstrumentoRepository instrumentoRepository;

    @Autowired
    public InstrumentoService(InstrumentoRepository instrumentoRepository) {
        this.instrumentoRepository = instrumentoRepository;
    }

    public Instrumento crearInstrumento(Instrumento instrumento) {
        return instrumentoRepository.save(instrumento);
    }

    public List<Instrumento> obtenerDiezInstrumentos() {
        return instrumentoRepository.findAll();
    }

    public Instrumento obtenerInstrumentoPorId(Long id) {
        Optional<Instrumento> instrumento = instrumentoRepository.findById(id);
        return instrumento.orElse(null);
    }

    public Instrumento actualizarInstrumento(Long id, Instrumento instrumento) {
        if (instrumentoRepository.existsById(id)) {
            instrumento.setId(id); // Asegura que el ID coincida
            return instrumentoRepository.save(instrumento);
        } else {
            return null; // Puedes manejar el caso en el que el instrumento no exista
        }
    }

    public void eliminarInstrumento(Long id) {
        instrumentoRepository.deleteById(id);
    }
}