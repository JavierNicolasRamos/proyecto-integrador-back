package com.proyecto.integrador.service;

import com.proyecto.integrador.commons.UserValidation;
import com.proyecto.integrador.dto.BuyerDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private static final Logger logger = LogManager.getLogger(BookingService.class);
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Booking createBooking(BookingDto bookingDto) {

        logger.info("Iniciando el proceso de creación de reserva...");
        try {
            Optional<Instrument> optionalInstrument = instrumentRepository.findById(bookingDto.getInstrumentDto().getId());
            Instrument instrument = optionalInstrument.orElseThrow(() -> new EntityNotFoundException("El instrumento no está disponible para la reserva"));

            this.userValidation.userValidation(bookingDto.getBuyerDto().getEmail(), instrument.getSeller().getEmail());

            Boolean bookingsExist= bookingRepository.hasOverlappingBookings(instrument.getId(), bookingDto.getBookingStart(), bookingDto.getBookingEnd());
            if (bookingsExist){
                logger.warn("El instrumento no está disponible para la reserva.");
                throw new EntityNotFoundException("El instrumento no está disponible para la reserva");
            }

            Booking booking = new Booking();

            booking.setUser(userService.findByEmail(bookingDto.getBuyerDto().getEmail()));
            booking.setInstrument(instrument);
            booking.setActiveBooking(bookingDto.getActiveBooking());
            booking.setBookingStart(bookingDto.getBookingStart());
            booking.setBookingEnd(bookingDto.getBookingEnd());
            booking.setDeleted(false);

            bookingRepository.save(booking);
            logger.info("Reserva creada con éxito.");

            this.emailService.sendEmail(booking.getUser().getEmail(), "Registro de reserva",
                    emailService.createBookingHtml(
                            booking.getUser().getName(),
                            booking.getUser().getSurname(),
                            booking.getInstrument().getName(),
                            booking.getBookingStart(),
                            booking.getInstrument().getSeller().getName(),
                            booking.getInstrument().getSeller().getPhone(),instrument.getSeller().getEmail()
            ));

            return booking;
        }
        catch (UserValidationException e ){
            throw new UserValidationException("No se puede reservar un instrumento que usted creo");
        }
        catch (Exception e) {
            logger.error("Se produjo un error al crear la reserva: " + e.getMessage());
            throw new CreateBookingException("Ocurrió un error al crear la reserva", e);
        }
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
                -> new NonExistentReserveException("No se encontró la reserva con ID" + bookingDto.getId()));
        try {
            logger.info("Reserva encontrada y será actualizada : " + booking);

            booking.setUser(userService.findByEmail(bookingDto.getBuyerDto().getEmail()));
            booking.setInstrument(instrumentService.getInstrumentById(bookingDto.getInstrumentDto().getId()));
            booking.setActiveBooking(bookingDto.getActiveBooking());
            booking.setBookingStart(bookingDto.getBookingStart());
            booking.setBookingEnd(bookingDto.getBookingEnd());

            return bookingRepository.save(booking);
        }
        catch (RuntimeException e){
          logger.error("Se produjo un error al actualizar la reserva: " + e.getMessage());
          throw new EditBookingException("Ocurrió un error al editar la reserva", e);
        }
    }
    
    @Transactional
    public void deleteBooking(Long id) {
        logger.info("Iniciando el proceso de eliminación de reserva con ID: " + id);
        try {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró la reserva con ID: " + id));

            booking.setDeleted(true);
            bookingRepository.save(booking);
            logger.info("Reserva con ID " + id + " marcada como eliminada.");

        } catch (Exception e) {
            logger.error("Se produjo un error al eliminar la reserva con ID: " + id + ". Error: " + e.getMessage());
            throw new DeleteReserveException("Error al eliminar la reserva con ID: " + id);
        }
    }

    public Optional<List<Booking>> ownReserve(BuyerDto buyerDto, Long instrumentId) {
       return this.bookingRepository.findByUserEmailAndInstrumentId(buyerDto.getEmail(), instrumentId);
    }


    @Scheduled(cron = "${cron.booking.expression}")
    public void updateBookings() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        List<Booking> bookingsToUpdate = bookingRepository.findByBookingEndAndDeletedIsFalse(yesterday);

        for (Booking booking : bookingsToUpdate) {
            booking.setActiveBooking(false);
            bookingRepository.save(booking);
        }
    }

    public List<LocalDate> findOccupiedDates(Long instrumentId) {
        List<Object[]> dateRanges = bookingRepository.findOccupiedDatesByInstrumentId(instrumentId);
        return extractAllDates(dateRanges);
    }

    private List<LocalDate> extractAllDates(List<Object[]> dateRanges) {
        List<LocalDate> allDates = new ArrayList<>();

        for (Object[] dateRange : dateRanges) {
            LocalDate startDate = (LocalDate) dateRange[0];
            LocalDate endDate = (LocalDate) dateRange[1];

            while (!startDate.isAfter(endDate)) {
                allDates.add(startDate);
                startDate = startDate.plusDays(1);
            }
        }

        return allDates;
    }
}