package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.BuyerDto;
import com.proyecto.integrador.dto.ReviewDto;
import com.proyecto.integrador.entity.Booking;
import com.proyecto.integrador.entity.Instrument;
import com.proyecto.integrador.entity.Review;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.repository.ReviewRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setBuyerDto(new BuyerDto());
        reviewDto.setInstrumentId(1L);
        reviewDto.setReviewName("Great Review");
        reviewDto.setReviewDescription("This instrument is amazing!");
        reviewDto.setScore(5.0);


        when(reviewRepository.findByBoyerEmailAndInstrumentId(any(), any())).thenReturn(Optional.empty());

        Booking booking = new Booking();
        booking.setUser(new User());
        booking.setInstrument(new Instrument());
        booking.setActiveBooking(false);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingService.ownReserve(reviewDto.getBuyerDto(), reviewDto.getInstrumentId())).thenReturn(Optional.of(bookings));

        Review savedReview = new Review();
        savedReview.setId(1L);

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);


        assertDoesNotThrow(() -> reviewService.createReview(reviewDto));

        verify(reviewRepository, times(1)).findByBoyerEmailAndInstrumentId(eq(reviewDto.getBuyerDto().getEmail()), eq(reviewDto.getInstrumentId()));
        verify(bookingService, times(1)).ownReserve(eq(reviewDto.getBuyerDto()), eq(reviewDto.getInstrumentId()));
        verify(reviewRepository, times(1)).save(any(Review.class));
        verify(instrumentService, times(1)).updateAvgScore(any(Review.class));
    }

    @Test
    void getAllReviewsUser() {
        Long userId = 1L;

        List<Review> mockReviews = Arrays.asList(
                new Review(),
                new Review()
        );

        when(reviewRepository.findByBoyer(userId)).thenReturn(mockReviews);


        List<Review> result = reviewService.getAllReviewsUser(userId);


        assertEquals(mockReviews.size(), result.size());
        assertEquals(mockReviews.get(0).getId(), result.get(0).getId());
        assertEquals(mockReviews.get(1).getId(), result.get(1).getId());

    }

    @Test
    void getAllReviewsInstrument() {

        Long instrumentId = 1L;

        List<Review> mockReviews = Arrays.asList(
                new Review(),
                new Review()
        );

        when(reviewRepository.findByInstrument(instrumentId)).thenReturn(mockReviews);


        List<Review> result = reviewService.getAllReviewsInstrument(instrumentId);


        assertEquals(mockReviews.size(), result.size());
        assertEquals(mockReviews.get(0).getId(), result.get(0).getId());
        assertEquals(mockReviews.get(1).getId(), result.get(1).getId());

    }





}