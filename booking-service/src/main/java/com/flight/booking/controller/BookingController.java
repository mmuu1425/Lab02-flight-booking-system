package com.flight.booking.controller;

import com.flight.booking.dto.BookingDTO;
import com.flight.booking.entity.Booking;
import com.flight.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public BookingDTO createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @GetMapping("/{bookingUid}")
    public BookingDTO getBookingById(@PathVariable UUID bookingUid) {
        return bookingService.getBookingById(bookingUid);
    }

    @GetMapping
    public List<BookingDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/user/{userUid}")
    public List<BookingDTO> getBookingsByUser(@PathVariable UUID userUid) {
        return bookingService.getBookingsByUser(userUid);
    }

    @GetMapping("/flight/{flightUid}")
    public List<BookingDTO> getBookingsByFlight(@PathVariable UUID flightUid) {
        return bookingService.getBookingsByFlight(flightUid);
    }

    @PutMapping("/{bookingUid}/status")
    public BookingDTO updateBookingStatus(@PathVariable UUID bookingUid, @RequestParam String status) {
        return bookingService.updateBookingStatus(bookingUid, status);
    }

    @PatchMapping("/{bookingUid}/cancel")
    public BookingDTO cancelBooking(@PathVariable UUID bookingUid) {
        return bookingService.cancelBooking(bookingUid);
    }

    @PutMapping("/{bookingUid}")
    public BookingDTO updateBooking(@PathVariable UUID bookingUid, @RequestBody Booking bookingDetails) {
        return bookingService.updateBooking(bookingUid, bookingDetails);
    }

    @DeleteMapping("/{bookingUid}")
    public ResponseEntity<?> deleteBooking(@PathVariable UUID bookingUid) {
        bookingService.deleteBooking(bookingUid);
        return ResponseEntity.ok().body("Booking deleted successfully");
    }
}