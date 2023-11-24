package com.proyecto.integrador.service;

import com.proyecto.integrador.commons.UserValidation;
import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.exception.DuplicateReviewException;
import com.proyecto.integrador.exception.UserValidationException;
import com.proyecto.integrador.repository.ReviewRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRespository reviewRespository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private InstrumentService instrumentService;

    @Autowired
    private UserValidation userValidation;

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    @Transactional
    public Review createReview(ReviewDto reviewDto) {
        try {
            logger.info("Iniciando el guardado de la reseña");

            this.reviewRespository.findByBoyerEmailAndInstrumentId(reviewDto.getBuyerDto().getEmail(), reviewDto.getInstrumentId())
                    .ifPresent(existingReview -> {
                        throw new DuplicateReviewException("Ya existe una reseña creada para este instrumento");
                    });

            List<Booking> bookings = bookingService.ownReserve(reviewDto.getBuyerDto(), reviewDto.getInstrumentId())
                    .orElseThrow(() -> new NotFoundException("Error al recuperar la lista de reservas"));

            if (bookings.isEmpty()){
                throw new UserValidationException("No se puede realizar una reseña de un instrumento que usted no reservó");
            }

            if (bookings.stream().anyMatch(booking -> !booking.getActiveBooking())) {
                Review review = new Review();
                review.setReviewName(reviewDto.getReviewName());
                review.setReviewDescription(reviewDto.getReviewDescription());
                review.setScore(reviewDto.getScore());
                review.setReviewDateTime(LocalDateTime.now());
                review.setDeleted(false);
                review.setBoyer(bookings.get(0).getUser());
                review.setInstrument(bookings.get(0).getInstrument());

                instrumentService.updateAvgScore(review);
                reviewRespository.save(review);

                logger.info("Reseña guardada con éxito");
                return review;
            } else {
                throw new UserValidationException("No se puede realizar una reseña de un instrumento si no posee una reserva finalizada del instrumento");
            }
        }
        catch(Exception e){
            logger.error("Error inesperado al crear la reseña: " + e.getMessage(), e);
            throw e;
        }
    }
}