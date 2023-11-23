package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.repository.ReviewRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReviewServiceTest {

    @Mock
    private ReviewRespository reviewRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private InstrumentService instrumentService;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createReview() {
    /*
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        Instrument instrument = new Instrument();
        instrument.setId(1L);
        booking.setInstrument(instrument);
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewName("Test Review");
        reviewDto.setReviewDescription("This is a test review");
        reviewDto.setScore(5.0);


        when(bookingService.getBooking(bookingId)).thenReturn(booking);
        when(reviewRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);


        Review result = reviewService.createReview(bookingId, reviewDto);


        assertNotNull(result);
        assertEquals(reviewDto.getReviewName(), result.getReviewName());
        assertEquals(reviewDto.getReviewDescription(), result.getReviewDescription());
        assertEquals(reviewDto.getScore(), result.getScore());
        assertNotNull(result.getReviewDateTime());
        assertFalse(result.getDeleted());
        assertEquals(booking, result.getBooking());


        verify(bookingService, times(1)).getBooking(bookingId);
        verify(instrumentService, times(1)).updateAvgScore(eq(reviewDto.getScore()), eq(instrument.getId()));
        verify(reviewRepository, times(1)).save(any());
        */

    }
}