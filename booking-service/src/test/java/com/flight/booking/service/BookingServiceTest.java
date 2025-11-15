package com.flight.booking.service;

import com.flight.booking.dto.BookingDTO;
import com.flight.booking.entity.Booking;
import com.flight.booking.repository.BookingRepository;
import com.flight.booking.exception.BookingNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private final UUID testUserUid = UUID.randomUUID();
    private final UUID testFlightUid = UUID.randomUUID();

    @Test
    void shouldCreateBooking() {
        // 给定
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        Booking savedBooking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        savedBooking.setBookingUid(UUID.randomUUID());

        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        // 当
        BookingDTO result = bookingService.createBooking(booking);

        // 那么
        assertThat(result.getUserUid()).isEqualTo(testUserUid);
        assertThat(result.getFlightUid()).isEqualTo(testFlightUid);
        assertThat(result.getStatus()).isEqualTo("CONFIRMED");
        verify(bookingRepository).save(booking);
    }

    @Test
    void shouldGetBookingById() {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        booking.setBookingUid(bookingUid);

        when(bookingRepository.findById(bookingUid)).thenReturn(Optional.of(booking));

        // 当
        BookingDTO result = bookingService.getBookingById(bookingUid);

        // 那么
        assertThat(result.getBookingUid()).isEqualTo(bookingUid);
        assertThat(result.getUserUid()).isEqualTo(testUserUid);
        assertThat(result.getFlightUid()).isEqualTo(testFlightUid);
    }

    @Test
    void shouldThrowExceptionWhenBookingNotFound() {
        // 给定
        UUID nonExistingUid = UUID.randomUUID();
        when(bookingRepository.findById(nonExistingUid)).thenReturn(Optional.empty());

        // 当 + 那么
        assertThatThrownBy(() -> bookingService.getBookingById(nonExistingUid))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessage("Booking not found: " + nonExistingUid);
    }

    @Test
    void shouldGetBookingsByUser() {
        // 给定
        UUID userUid = UUID.randomUUID();
        Booking booking1 = new Booking(userUid, testFlightUid, "CONFIRMED");
        Booking booking2 = new Booking(userUid, UUID.randomUUID(), "CANCELLED");
        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(bookingRepository.findByUserUid(userUid)).thenReturn(bookings);

        // 当
        List<BookingDTO> result = bookingService.getBookingsByUser(userUid);

        // 那么
        assertThat(result).hasSize(2);
        assertThat(result).extracting(BookingDTO::getUserUid)
                .containsExactly(userUid, userUid);
    }

    @Test
    void shouldUpdateBookingStatus() {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        booking.setBookingUid(bookingUid);

        when(bookingRepository.findById(bookingUid)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // 当
        BookingDTO result = bookingService.updateBookingStatus(bookingUid, "COMPLETED");

        // 那么
        assertThat(result.getStatus()).isEqualTo("COMPLETED");
        verify(bookingRepository).save(booking);
    }

    @Test
    void shouldCancelBooking() {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        booking.setBookingUid(bookingUid);

        when(bookingRepository.findById(bookingUid)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        // 当
        BookingDTO result = bookingService.cancelBooking(bookingUid);

        // 那么
        assertThat(result.getStatus()).isEqualTo("CANCELLED");
        verify(bookingRepository).save(booking);
    }

    @Test
    void shouldDeleteBooking() {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        booking.setBookingUid(bookingUid);

        when(bookingRepository.findById(bookingUid)).thenReturn(Optional.of(booking));
        doNothing().when(bookingRepository).delete(booking);

        // 当
        bookingService.deleteBooking(bookingUid);

        // 那么
        verify(bookingRepository).delete(booking);
    }
}