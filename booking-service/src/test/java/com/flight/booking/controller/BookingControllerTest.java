package com.flight.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.booking.dto.BookingDTO;
import com.flight.booking.entity.Booking;
import com.flight.booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID testUserUid = UUID.randomUUID();
    private final UUID testFlightUid = UUID.randomUUID();

    @Test
    void shouldCreateBooking() throws Exception {
        // 给定
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        BookingDTO bookingDTO = new BookingDTO(UUID.randomUUID(), testUserUid, testFlightUid, "CONFIRMED");

        when(bookingService.createBooking(any(Booking.class))).thenReturn(bookingDTO);

        // 当 + 那么
        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$.flightUid").value(testFlightUid.toString()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldGetBookingById() throws Exception {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        BookingDTO bookingDTO = new BookingDTO(bookingUid, testUserUid, testFlightUid, "CONFIRMED");
        when(bookingService.getBookingById(bookingUid)).thenReturn(bookingDTO);

        // 当 + 那么
        mockMvc.perform(get("/api/bookings/" + bookingUid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingUid").value(bookingUid.toString()))
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$.flightUid").value(testFlightUid.toString()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldGetAllBookings() throws Exception {
        // 给定
        BookingDTO booking1 = new BookingDTO(UUID.randomUUID(), testUserUid, testFlightUid, "CONFIRMED");
        BookingDTO booking2 = new BookingDTO(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), "PENDING");
        List<BookingDTO> bookings = Arrays.asList(booking1, booking2);

        when(bookingService.getAllBookings()).thenReturn(bookings);

        // 当 + 那么
        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status").exists())
                .andExpect(jsonPath("$[1].status").exists());
    }

    @Test
    void shouldGetBookingsByUser() throws Exception {
        // 给定
        BookingDTO booking1 = new BookingDTO(UUID.randomUUID(), testUserUid, testFlightUid, "CONFIRMED");
        BookingDTO booking2 = new BookingDTO(UUID.randomUUID(), testUserUid, UUID.randomUUID(), "CANCELLED");
        List<BookingDTO> bookings = Arrays.asList(booking1, booking2);

        when(bookingService.getBookingsByUser(testUserUid)).thenReturn(bookings);

        // 当 + 那么
        mockMvc.perform(get("/api/bookings/user/" + testUserUid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$[1].userUid").value(testUserUid.toString()));
    }

    @Test
    void shouldGetBookingsByFlight() throws Exception {
        // 给定
        BookingDTO booking1 = new BookingDTO(UUID.randomUUID(), testUserUid, testFlightUid, "CONFIRMED");
        BookingDTO booking2 = new BookingDTO(UUID.randomUUID(), UUID.randomUUID(), testFlightUid, "PENDING");
        List<BookingDTO> bookings = Arrays.asList(booking1, booking2);

        when(bookingService.getBookingsByFlight(testFlightUid)).thenReturn(bookings);

        // 当 + 那么
        mockMvc.perform(get("/api/bookings/flight/" + testFlightUid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].flightUid").value(testFlightUid.toString()))
                .andExpect(jsonPath("$[1].flightUid").value(testFlightUid.toString()));
    }

    @Test
    void shouldUpdateBookingStatus() throws Exception {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        BookingDTO updatedBooking = new BookingDTO(bookingUid, testUserUid, testFlightUid, "COMPLETED");
        when(bookingService.updateBookingStatus(bookingUid, "COMPLETED")).thenReturn(updatedBooking);

        // 当 + 那么
        mockMvc.perform(put("/api/bookings/" + bookingUid + "/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()));
    }

    @Test
    void shouldCancelBooking() throws Exception {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        BookingDTO cancelledBooking = new BookingDTO(bookingUid, testUserUid, testFlightUid, "CANCELLED");
        when(bookingService.cancelBooking(bookingUid)).thenReturn(cancelledBooking);

        // 当 + 那么
        mockMvc.perform(patch("/api/bookings/" + bookingUid + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()));
    }

    @Test
    void shouldUpdateBooking() throws Exception {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        Booking bookingDetails = new Booking();
        bookingDetails.setStatus("RESCHEDULED");

        BookingDTO updatedBooking = new BookingDTO(bookingUid, testUserUid, testFlightUid, "RESCHEDULED");
        when(bookingService.updateBooking(eq(bookingUid), any(Booking.class))).thenReturn(updatedBooking);

        // 当 + 那么
        mockMvc.perform(put("/api/bookings/" + bookingUid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESCHEDULED"))
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()));
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        // 给定
        UUID bookingUid = UUID.randomUUID();
        doNothing().when(bookingService).deleteBooking(bookingUid);

        // 当 + 那么
        mockMvc.perform(delete("/api/bookings/" + bookingUid))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking deleted successfully"));

        verify(bookingService).deleteBooking(bookingUid);
    }
}