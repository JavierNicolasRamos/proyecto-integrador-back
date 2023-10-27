package com.proyecto.integrador.service;


import com.proyecto.integrador.dto.ReservaDto;
import com.proyecto.integrador.entity.Instrumento;
import com.proyecto.integrador.entity.Reserva;
import com.proyecto.integrador.exception.*;
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


        Optional<Instrumento> optionalInstrumento = instrumentoRepository.findById(reservaDto.getInstrumento().getId());
        Instrumento instrumento = optionalInstrumento.orElseThrow(() -> new EntityNotFoundException("El instrumento no está disponible para la reserva"));
        if (!instrumento.getDisponible()) {
            throw new EntityNotFoundException("El instrumento no está disponible para la reserva");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(reservaDto.getUsuario());
        reserva.setInstrumento(instrumento);
        //COMUNICAR LOS INFORMAR AL EQUIPO DE FRONT
        reserva.setReservaActiva(reservaDto.getReservaActiva()); //TODO: ver si lo pongo true o esperar al front (a confirmar)
        reserva.setInicioReserva(reservaDto.getInicioReserva()); //TODO: ver si lo pongo localdate.now() o esperar al front (a confirmar)
        reserva.setFinReserva(reservaDto.getFinReserva());  //TODO: ver si lo pongo localdate +5 dias o esperar al front (a confirmar)


        instrumento.setDisponible(false);
        instrumentoRepository.save(instrumento);

        reservaRepository.save(reserva);
        return reserva;
    }


    public Reserva obtenerReserva(Long id) {
        Optional<Reserva> reservaOptional = Optional.ofNullable(reservaRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("No se encontró la reserva")));
        return reservaOptional.get();
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

        Optional<Reserva> reservaOptional = Optional.ofNullable(reservaRepository.findById(reservaDto.getId()).orElseThrow(()
                -> new EntityNotFoundException("No se encontró la reserva")));

        if (reservaOptional.isPresent()) {

            Reserva reservaExistente = reservaOptional.get();
            reservaExistente.setUsuario(reservaDto.getUsuario());
            reservaExistente.setInstrumento(reservaDto.getInstrumento());
            reservaExistente.setReservaActiva(reservaDto.getReservaActiva());
            reservaExistente.setInicioReserva(reservaDto.getInicioReserva());
            reservaExistente.setFinReserva(reservaDto.getFinReserva());
            Optional<Instrumento> optionalInstrumento = instrumentoRepository.findById(reservaDto.getInstrumento().getId());
            if (optionalInstrumento.isPresent()) {
                Instrumento instrumento = optionalInstrumento.get();
                instrumento.setDisponible(false);

                instrumentoRepository.save(instrumento);
            } else {
                throw new InstrumentoNotFoundException("No se encontró el instrumento con el ID: " + reservaDto.getInstrumento().getId());
            }

            return reservaRepository.save(reservaExistente);
        } else {
            throw new NonExistentReservaException("No se encontró la reserva con ID: " + id);
        }
    }




    @Transactional
    public void eliminarReserva(Long id) {
        try {
            Reserva reserva = reservaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la reserva con ID: " + id));

            reserva.setEliminado(true);

            if (reserva.getInstrumento() != null) {
                reserva.getInstrumento().setDisponible(true);
                instrumentoRepository.save(reserva.getInstrumento());
            } else {
               throw new InstrumentoNotFoundException("No se encontró un instrumento asociado a la reserva con ID: " + id);
            }
            reservaRepository.save(reserva);

        } catch (Exception e) {
            throw new EliminacionReservaException("Error al eliminar la reserva con ID: " + id);
        }
    }


}
