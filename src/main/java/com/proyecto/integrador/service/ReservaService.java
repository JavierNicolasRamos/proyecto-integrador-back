package com.proyecto.integrador.service;


import com.proyecto.integrador.dto.ReservaDto;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.exception.EliminacionReservaException;
import com.proyecto.integrador.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;


    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }


    public Reserva crearReserva(ReservaDto reservaDto) {
        Reserva reserva = new Reserva();


        reserva.setUsuario(reservaDto.getUsuario());
        reserva.setInstrumento(reservaDto.getInstrumento());
        reserva.setReservaActiva(reservaDto.getReservaActiva());
        reserva.setInicioReserva(reservaDto.getInicioReserva());
        reserva.setFinReserva(reservaDto.getFinReserva());


        return reservaRepository.save(reserva);
    }


    public Reserva obtenerReserva(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }


    public Reserva actualizarReserva(Long id, ReservaDto reservaDto) {

        Optional<Reserva> reservaOptional = reservaRepository.findById(id);

        if (reservaOptional.isPresent()) {
            Reserva reservaExistente = reservaOptional.get();


            reservaExistente.setUsuario(reservaDto.getUsuario());
            reservaExistente.setInstrumento(reservaDto.getInstrumento());
            reservaExistente.setReservaActiva(reservaDto.getReservaActiva());
            reservaExistente.setInicioReserva(reservaDto.getInicioReserva());
            reservaExistente.setFinReserva(reservaDto.getFinReserva());


            return reservaRepository.save(reservaExistente);
        } else {
            return null;
        }
    }


    public void eliminarReserva(Long id) {
        try {
            reservaRepository.deleteById(id);
        } catch (Exception e) {
            throw new EliminacionReservaException("Error al eliminar la reserva con ID: " + id);
        }
    }

}
