package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.exception.DuplicateReviewException;
import com.proyecto.integrador.repository.ReviewRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewService {
    @Autowired
    private ReviewRespository reviewRespository;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private InstrumentService instrumentService;

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);


    @Transactional
    public Review createReview(Long bookingId, ReviewDto reviewDto) {
        try {
            logger.info("Iniciando el guardado de la reseña");
            Booking booking = bookingService.getBooking(bookingId);

            if(booking.getReview() != null){
                throw new DuplicateReviewException("La reserva con id:" + bookingId  + "ya tiene una reseña");
            }

            Review review = new Review();
            review.setReviewName(reviewDto.getReviewName());
            review.setReviewDescription(reviewDto.getReviewDescription());
            review.setScore(reviewDto.getScore());
            review.setReviewDateTime(LocalDateTime.now());
            review.setDeleted(false);
            review.setBooking(booking);

            instrumentService.updateAvgScore(reviewDto.getScore(), booking.getInstrument().getId());//LLamo al service para actualizar el Instrument reviewCount y el Instrumen Score
            reviewRespository.save(review);

            logger.info("Reseña guardada con exito");
            return review;
        } catch (DuplicateReviewException e) {
            logger.error("La reserva con id:" + bookingId  + "ya tiene una reseña");
            throw e;
        }catch(Exception e){
            logger.error("Error inesperado al crear la reseña: " + e.getMessage(), e);
            throw e;
        }

    }
}