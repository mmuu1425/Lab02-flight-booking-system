package com.flight.booking.service;

import com.flight.booking.dto.BookingDTO;
import com.flight.booking.entity.Booking;
import com.flight.booking.repository.BookingRepository;
import com.flight.booking.exception.BookingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public BookingDTO createBooking(Booking booking) {
        Booking savedBooking = bookingRepository.save(booking);
        return convertToDTO(savedBooking);
    }

    public BookingDTO getBookingById(UUID bookingUid) {
        Booking booking = bookingRepository.findById(bookingUid)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + bookingUid));
        return convertToDTO(booking);
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByUser(UUID userUid) {
        return bookingRepository.findByUserUid(userUid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByFlight(UUID flightUid) {
        return bookingRepository.findByFlightUid(flightUid).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO updateBookingStatus(UUID bookingUid, String status) {
        Booking booking = bookingRepository.findById(bookingUid)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + bookingUid));
        booking.setStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDTO(updatedBooking);
    }

    public BookingDTO cancelBooking(UUID bookingUid) {
        Booking booking = bookingRepository.findById(bookingUid)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + bookingUid));
        booking.setStatus("CANCELLED");
        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDTO(updatedBooking);
    }

    public BookingDTO updateBooking(UUID bookingUid, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(bookingUid)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + bookingUid));

        if (bookingDetails.getStatus() != null) {
            booking.setStatus(bookingDetails.getStatus());
        }

        Booking updatedBooking = bookingRepository.save(booking);
        return convertToDTO(updatedBooking);
    }

    public void deleteBooking(UUID bookingUid) {
        Booking booking = bookingRepository.findById(bookingUid)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found: " + bookingUid));
        bookingRepository.delete(booking);
    }

    private BookingDTO convertToDTO(Booking booking) {
        return new BookingDTO(
                booking.getBookingUid(),
                booking.getUserUid(),
                booking.getFlightUid(),
                booking.getStatus()
        );
    }
}