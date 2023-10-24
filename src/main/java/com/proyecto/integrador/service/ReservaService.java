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

        //TODO: cambiar variable a optional (Hecho)
        Optional<Instrumento> optionalInstrumento = instrumentoRepository.findById(reservaDto.getInstrumento().getId());
        if (!optionalInstrumento.isPresent() || !optionalInstrumento.get().getDisponible()) { //Si no esta presente, arrojar una excepcion con orElseThrow, luego validar en el if si el instrumento no esta disponible.
            throw new EntityNotFoundException("El instrumento no está disponible para la reserva");
        }


        Reserva reserva = new Reserva();
        reserva.setUsuario(reservaDto.getUsuario());
        reserva.setInstrumento(reservaDto.getInstrumento());
        //COMUNICAR LOS INFORMAR AL EQUIPO DE FRONT
        reserva.setReservaActiva(reservaDto.getReservaActiva()); //TODO: ver si lo pongo true o esperar al front (a confirmar)
        reserva.setInicioReserva(reservaDto.getInicioReserva()); //TODO: ver si lo pongo localdate.now() o esperar al front (a confirmar)
        reserva.setFinReserva(reservaDto.getFinReserva());  //TODO: ver si lo pongo localdate +5 dias o esperar al front (a confirmar)

        //TODO: Crear logica para actualizar campo disponible de instrumento (Hecho)
        Instrumento instrumento = optionalInstrumento.get();
        instrumento.setDisponible(false); //Falta persistir los cambios en la base

        reservaRepository.save(reserva);
        return reserva;
    }



    public Reserva obtenerReserva(Long id) {  //TODO: Acá habia que hacer algo parecido a la linea 89 (A chequear)
        try {
            Optional<Reserva> reservaOptional = reservaRepository.findById(id);

            if (reservaOptional.isPresent()) {
                return reservaOptional.get();
            } else {
                throw new EntityNotFoundException("No se encontró la reserva con ID: " + id);
            }
        } catch (Exception e) {
            throw new ObtenerReservaException("Ocurrió un error al obtener la reserva con ID: " + id, e);
        }
    }


//Se podria realizar de la siguiente forma:
//    public Reserva obtenerReservaEjemplo(Long id) {
//       Optional<Reserva> reservaOptional = Optional.ofNullable(reservaRepository.findById(id).orElseThrow(()
//            -> new EntityNotFoundException("No se encontró la reserva")));
//       return reservaOptional.get();
//    }


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
            //TODO: Crear logica para actualizar campo disponible de instrumento (Hecho)
            Instrumento instrumento = reservaDto.getInstrumento(); //Traer el instrumento de la base, utilizando un optional
            if (instrumento != null) { //Utilizar isPreset, en caso de que no se encuentre el instrumento, arrojar una excepcion
                instrumento.setDisponible(false); //Falta persistir los cambios en la base
            }
            return reservaRepository.save(reservaExistente);
        } else {
            throw new NonExistentReservaException("No se encontró la reserva con ID: " + id);
        }
    }

    @Transactional
    public void eliminarReserva(Long id) {           //TODO: Eliminar reserva y su estado pase a true (Hecho), Pasar a disponible el instrumento eliminado (Hecho)
        try {
            Optional<Reserva> reservaOptional = reservaRepository.findById(id);

            if (reservaOptional.isPresent()) {
                Reserva reserva = reservaOptional.get();

                reserva.setEliminado(true);

                Instrumento instrumento = reserva.getInstrumento();
                if (instrumento != null) { //El instrumento no puede ser null, en caso de que sea null, se debe arrojar una excepcion
                    instrumento.setDisponible(true); //Falta persistir los cambios en la base
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
