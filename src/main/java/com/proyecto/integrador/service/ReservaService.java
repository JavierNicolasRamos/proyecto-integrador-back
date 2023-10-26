package com.proyecto.integrador.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(ReservaService.class);



    @Transactional
    public Reserva crearReserva(ReservaDto reservaDto) {

        logger.info("Iniciando el proceso de creación de reserva...");
        try {
            Optional<Instrumento> optionalInstrumento = instrumentoRepository.findById(reservaDto.getInstrumento().getId());
            Instrumento instrumento = optionalInstrumento.orElseThrow(() -> new EntityNotFoundException("El instrumento no está disponible para la reserva"));

            logger.info("Se va a modificar el objeto Instrumento, atributo disponible: " + instrumento.getDisponible());

            if (!instrumento.getDisponible()) {
                logger.warn("El instrumento no está disponible para la reserva.");
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
            logger.info("El objeto Instrumento fue modificado correctamente, atributo disponible: " + instrumento.getDisponible());

            reservaRepository.save(reserva);
            logger.info("Reserva creada con éxito.");

            return reserva;
        } catch (Exception e) {
            logger.error("Se produjo un error al crear la reserva: " + e.getMessage());
            throw e;
        }
    }


    public Reserva obtenerReserva(Long id) {
        logger.info("Iniciando el proceso de obtención de la reserva con ID: " + id);
        try {
            Optional<Reserva> reservaOptional = Optional.ofNullable(reservaRepository.findById(id).orElseThrow(()
                    -> {
                logger.error("Se produjo una excepción, No se encontró la reserva");
                return new EntityNotFoundException("No se encontró la reserva");
            }));
            logger.info("Reserva obtenida con éxito. ID: " + reservaOptional.get().getId());
            return reservaOptional.get();
        } catch (EntityNotFoundException e) {
            logger.error("Se produjo una excepción al obtener la reserva: " + e.getMessage());
            throw e;
        }
    }


    public List<Reserva> listarReservas() {
        logger.info("Iniciando el proceso de listado de reservas...");

        try {
            List<Reserva> reservas = reservaRepository.findAll();
            logger.info("Se obtuvieron " + reservas.size() + " reservas.");
            return reservas;
        } catch (Exception e) {
            logger.error("Ocurrió un error al listar las reservas: " + e.getMessage());
            throw new ListarReservasException("Ocurrió un error al listar las reservas", e);
        }
    }


    @Transactional
    public Reserva actualizarReserva(Long id, ReservaDto reservaDto) {

        logger.info("Iniciando el proceso de actualización de reserva con ID: " + id);
    try {
        Optional<Reserva> reservaOptional = Optional.ofNullable(reservaRepository.findById(reservaDto.getId()).orElseThrow(()
                -> new EntityNotFoundException("No se encontró la reserva")));

        if (reservaOptional.isPresent()) {

            Reserva reservaExistente = reservaOptional.get();
            logger.info("Reserva encontrada y será actualizada : " + reservaExistente);

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
                logger.info("Instrumento actualizado para reserva. ID: " + instrumento.getId());
            } else {
                logger.error("No se encontró el instrumento con el ID: " + reservaDto.getInstrumento().getId());
                throw new InstrumentoNotFoundException("No se encontró el instrumento con el ID: " + reservaDto.getInstrumento().getId());
            }

            logger.info("Reserva actualizada con éxito. ID: " + reservaExistente.getId());
            return this.reservaRepository.save(reservaExistente);
        } else {
            logger.error("No se encontró la reserva con ID: " + id);
            throw new NonExistentReservaException("No se encontró la reserva con ID: " + id);
        }
    }catch (Exception e) {
        logger.error("Se produjo un error al actualizar la reserva: " + e.getMessage());
        throw e;
        }
    }




    @Transactional
    public void eliminarReserva(Long id) {
        logger.info("Iniciando el proceso de eliminación de reserva con ID: " + id);

        try {
            Reserva reserva = reservaRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la reserva con ID: " + id));

            reserva.setEliminado(true);

            if (reserva.getInstrumento() != null) {
                reserva.getInstrumento().setDisponible(true);
                instrumentoRepository.save(reserva.getInstrumento());
                logger.info("Instrumento asociado a la reserva con ID " + id + " marcado como disponible.");
            } else {
                logger.error("No se encontró un instrumento asociado a la reserva con ID: " + id);
               throw new InstrumentoNotFoundException("No se encontró un instrumento asociado a la reserva con ID: " + id);
            }
            reservaRepository.save(reserva);
            logger.info("Reserva con ID " + id + " marcada como eliminada.");

        } catch (Exception e) {
            logger.error("Se produjo un error al eliminar la reserva con ID: " + id + ". Error: " + e.getMessage());
            throw new EliminacionReservaException("Error al eliminar la reserva con ID: " + id);
        }
    }


}
