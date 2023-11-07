package com.proyecto.integrador.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.proyecto.integrador.dto.BookingDto;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.exception.*;
import com.proyecto.integrador.repository.InstrumentRepository;
import com.proyecto.integrador.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private static final Logger logger = LogManager.getLogger(BookingService.class);
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Transactional
    public Booking createBooking(BookingDto bookingDto) {

        logger.info("Iniciando el proceso de creación de reserva...");
        try {
            Optional<Instrument> optionalInstrument = instrumentRepository.findById(bookingDto.getInstrument().getId());
            Instrument instrument = optionalInstrument.orElseThrow(() -> new EntityNotFoundException("El instrumento no está disponible para la reserva"));

            logger.info("Se va a modificar el objeto Instrumento, atributo disponible: " + instrument.getAvailable());

            if (!instrument.getAvailable()) {
                logger.warn("El instrumento no está disponible para la reserva.");
                throw new EntityNotFoundException("El instrumento no está disponible para la reserva");
            }

            Booking booking = getBooking(bookingDto, instrument);

            instrument.setAvailable(false);
            instrumentRepository.save(instrument);
            logger.info("El objeto Instrumento fue modificado correctamente, atributo disponible: " + instrument.getAvailable());

            bookingRepository.save(booking);
            logger.info("Reserva creada con éxito.");

            return booking;
        } catch (Exception e) {
            logger.error("Se produjo un error al crear la reserva: " + e.getMessage());
            throw e; //TODO: ver si se puede hacer un throw new CreateReserveException("Ocurrió un error al crear la reserva", e);
        }
    }

    @NotNull
    private static Booking getBooking(@NotNull BookingDto bookingDto, Instrument instrument) {
        Booking booking = new Booking();
        booking.setUser(bookingDto.getUser());
        booking.setInstrument(instrument);

        booking.setActiveBooking(bookingDto.getActiveBooking()); //TODO: ver si lo pongo true o esperar al front (a confirmar)
        booking.setBookingStart(bookingDto.getBookingStart()); //TODO: ver si lo pongo localdate.now() o esperar al front (a confirmar)
        booking.setBookingEnd(bookingDto.getBookingEnd());  //TODO: ver si lo pongo localdate +5 dias o esperar al front (a confirmar)
        return booking;
    }

    public Booking getBooking(Long id) {
        return bookingRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("No se encontró la reserva"));
    }

    public List<Booking> listBooking() {
        logger.info("Iniciando el proceso de listado de reservas...");

        try {
            List<Booking> bookings = bookingRepository.findAll();
            logger.info("Se obtuvieron " + bookings.size() + " reservas.");
            return bookings;
        } catch (Exception e) {
            logger.error("Ocurrió un error al listar las reservas: " + e.getMessage());
            throw new ListReservesException("Ocurrió un error al listar las reservas", e);
        }
    }

    @Transactional
    public Booking updateBooking(@NotNull BookingDto bookingDto) {
        logger.info("Iniciando el proceso de actualización de reserva con ID: " + bookingDto.getId());

        Booking booking = bookingRepository.findById(bookingDto.getId()).orElseThrow(()
                -> new NonExistentReserveException("No se encontró la reserva"));
        try {
            logger.info("Reserva encontrada y será actualizada : " + booking);
            booking.setUser(bookingDto.getUser());
            booking.setInstrument(bookingDto.getInstrument());
            booking.setActiveBooking(bookingDto.getActiveBooking());
            booking.setBookingStart(bookingDto.getBookingStart());
            booking.setBookingEnd(bookingDto.getBookingEnd());
            Optional<Instrument> optionalInstrument = instrumentRepository.findById(bookingDto.getInstrument().getId());
            if (optionalInstrument.isPresent()) {
                Instrument instrument = optionalInstrument.get();
                instrument.setAvailable(false);
                instrumentRepository.save(instrument);
                logger.info("Instrumento actualizado para reserva. ID: " + instrument.getId());
            } else {
                logger.error("No se encontró el instrumento con el ID: " + bookingDto.getInstrument().getId());
                throw new InstrumentNotFoundException("No se encontró el instrumento con el ID: " + bookingDto.getInstrument().getId());
            }
            return bookingRepository.save(booking);
        }
        catch (RuntimeException e){
          logger.error("Se produjo un error al actualizar la reserva: " + e.getMessage());
          throw e; //TODO: ver si se puede hacer un throw new EditReserveException("Ocurrió un error al editar la reserva", e);
        }
    }
    
    @Transactional
    public void deleteBooking(Long id) {
        logger.info("Iniciando el proceso de eliminación de reserva con ID: " + id);
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la reserva con ID: " + id));
            booking.setDeleted(true);

            if (booking.getInstrument() != null) {
                booking.getInstrument().setAvailable(true);
                instrumentRepository.save(booking.getInstrument());
                logger.info("Instrumento asociado a la reserva con ID " + id + " marcado como disponible.");
            } else {
                logger.error("No se encontró un instrumento asociado a la reserva con ID: " + id);
                throw new InstrumentNotFoundException("No se encontró un instrumento asociado a la reserva con ID: " + id);
            }
            bookingRepository.save(booking);
            logger.info("Reserva con ID " + id + " marcada como eliminada.");

        } catch (Exception e) {
            logger.error("Se produjo un error al eliminar la reserva con ID: " + id + ". Error: " + e.getMessage());
            throw new DeleteReserveException("Error al eliminar la reserva con ID: " + id);
        }
    }
}