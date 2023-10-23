package com.proyecto.integrador.service;


import com.proyecto.integrador.dto.ReservaDto;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.exception.EliminacionReservaException;
import com.proyecto.integrador.exception.ListarReservasException;
import com.proyecto.integrador.exception.NonExistentReservaException;
import com.proyecto.integrador.exception.ObtenerReservaException;
import com.proyecto.integrador.repository.InstrumentoRepository;
import com.proyecto.integrador.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private InstrumentoRepository instrumentoRepository;

    @Transactional
    public Reserva crearReserva(ReservaDto reservaDto) {
        Instrumento instrumento = reservaDto.getInstrumento();

        if (instrumento.getDisponible() != null && !instrumento.getDisponible()) {
            throw new EntityNotFoundException("El instrumento no está disponible para la reserva");
        }

        Optional<Reserva> reservaOptional = Optional.ofNullable(reservaRepository.findById(reservaDto.getId()).orElseThrow(() -> new EntityNotFoundException("No se encontró la reserva")));

        Reserva reserva = new Reserva();
        reserva.setUsuario(reservaDto.getUsuario());
        reserva.setInstrumento(reservaDto.getInstrumento());
        reserva.setReservaActiva(reservaDto.getReservaActiva());
        reserva.setInicioReserva(reservaDto.getInicioReserva());
        reserva.setFinReserva(reservaDto.getFinReserva());

        reservaRepository.save(reserva);
        return reserva;
    }


    public Reserva obtenerReserva(Long id) {
        try {
            return reservaRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new ObtenerReservaException("Ocurrió un error al obtener la reserva con ID: " + id, e);
        }
    }


    public List<Reserva> listarReservas() {
        try {
            return reservaRepository.findAll();
        } catch (Exception e) {
            throw new ListarReservasException("Ocurrió un error al listar las reservas", e);
        }
    }


    @Transactional
    public Reserva actualizarReserva(Long id, ReservaDto reservaDto) {

        Optional<Reserva> reservaOptional = reservaRepository.findById(id);

        if (reservaOptional.isPresent()) {

            Reserva reservaExistente = reservaOptional.get();
            reservaExistente.setUsuario(reservaDto.getUsuario());
            reservaExistente.setInstrumento(reservaDto.getInstrumento());
            reservaExistente.setReservaActiva(reservaDto.getReservaActiva());
            reservaExistente.setInicioReserva(reservaDto.getInicioReserva());
            reservaExistente.setFinReserva(reservaDto.getFinReserva());

            reservaRepository.save(reservaExistente);
            return reservaRepository.save(reservaExistente);
        } else {
            throw new NonExistentReservaException("No se encontró la reserva con ID: " + id);
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
