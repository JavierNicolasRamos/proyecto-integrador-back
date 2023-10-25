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
        //Si no esta presente, arrojar una excepcion con orElseThrow, luego validar en el if si el instrumento no esta disponible.  (listo)
        if (!instrumento.getDisponible()) {
            throw new EntityNotFoundException("El instrumento no está disponible para la reserva");
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(reservaDto.getUsuario());
        reserva.setInstrumento(reservaDto.getInstrumento());
        //COMUNICAR LOS INFORMAR AL EQUIPO DE FRONT
        reserva.setReservaActiva(reservaDto.getReservaActiva()); //TODO: ver si lo pongo true o esperar al front (a confirmar)
        reserva.setInicioReserva(reservaDto.getInicioReserva()); //TODO: ver si lo pongo localdate.now() o esperar al front (a confirmar)
        reserva.setFinReserva(reservaDto.getFinReserva());  //TODO: ver si lo pongo localdate +5 dias o esperar al front (a confirmar)


        instrumento.setDisponible(false);
        instrumentoRepository.save(instrumento); //Falta persistir los cambios en la base (listo)

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
            Optional<Instrumento> optionalInstrumento = instrumentoRepository.findById(reservaDto.getInstrumento().getId()); // Traer el instrumento de la base, utilizando un optional(listo)
            if (optionalInstrumento.isPresent()) {
                Instrumento instrumento = optionalInstrumento.get();
                instrumento.setDisponible(false); // Actualizar la disponibilidad del instrumento (listo)

                instrumentoRepository.save(instrumento);  // Persistir los cambios en el objeto Instrumento en la base de datos (listo)
            } else {
                throw new InstrumentoNotFoundException("No se encontró el instrumento con el ID: " + reservaDto.getInstrumento().getId());
            }

            // Persistir los cambios en la reserva (listo)
            return reservaRepository.save(reservaExistente);
        } else {
            throw new NonExistentReservaException("No se encontró la reserva con ID: " + id);
        }
    }




    @Transactional
    public void eliminarReserva(Long id) {
        try {
            Optional<Reserva> reservaOptional = reservaRepository.findById(id);

            if (reservaOptional.isPresent()) {
                Reserva reserva = reservaOptional.get();

                reserva.setEliminado(true);

                Instrumento instrumento = reserva.getInstrumento();

                if (instrumento != null) {
                    instrumento.setDisponible(true);
                    instrumentoRepository.save(instrumento); // Persistir los cambios en la disponibilidad del instrumento (listo)
                } else {
                    throw new InstrumentoNotFoundException("No se encontró un instrumento asociado a la reserva con ID: " + id);
                }
                reservaRepository.save(reserva);
            } else {
                throw new EntityNotFoundException("No se encontró la reserva con ID: " + id);
            }
        } catch (Exception e) {
            throw new EliminacionReservaException("Error al eliminar la reserva con ID: " + id);
        }
    }


}
