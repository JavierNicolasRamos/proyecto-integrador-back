package com.proyecto.integrador.service;


import com.proyecto.integrador.dto.ReservaDto;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.exception.DuplicateInstrumentException;
import com.proyecto.integrador.exception.EliminacionReservaException;
import com.proyecto.integrador.repository.InstrumentoRepository;
import com.proyecto.integrador.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;


    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }




    public void eliminarReserva(Long id) {
        try {
            reservaRepository.deleteById(id);
        } catch (Exception e) {
            throw new EliminacionReservaException("Error al eliminar la reserva con ID: " + id);
        }
    }

}
