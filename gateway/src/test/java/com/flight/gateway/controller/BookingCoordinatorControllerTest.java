package com.flight.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.gateway.dto.BookingRequest;
import com.flight.gateway.dto.CompleteBookingResponse;
import com.flight.gateway.service.BookingCoordinatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(BookingCoordinatorController.class)
class BookingCoordinatorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookingCoordinatorService bookingCoordinatorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateCompleteBookingSuccessfully() throws Exception {
        // 给定
        BookingRequest request = new BookingRequest("alex", "CA123");
        CompleteBookingResponse response = CompleteBookingResponse.success(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(bookingCoordinatorService.createCompleteBooking(anyString(), anyString()))
                .thenReturn(Mono.just(response));

        // 当 + 那么
        webTestClient.post()
                .uri("/api/bookings/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.message").isEqualTo("Booking created successfully");
    }

    @Test
    void shouldReturnBadRequestWhenBookingFails() throws Exception {
        // 给定
        BookingRequest request = new BookingRequest("nonexistent", "CA123");
        CompleteBookingResponse response = CompleteBookingResponse.failure("User not found");

        when(bookingCoordinatorService.createCompleteBooking(anyString(), anyString()))
                .thenReturn(Mono.just(response));

        // 当 + 那么
        webTestClient.post()
                .uri("/api/bookings/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("User not found");
    }

    @Test
    void shouldCreateCompleteBookingByParamsSuccessfully() throws Exception {
        // 给定
        CompleteBookingResponse response = CompleteBookingResponse.success(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(bookingCoordinatorService.createCompleteBooking(anyString(), anyString()))
                .thenReturn(Mono.just(response));

        // 当 + 那么
        webTestClient.post()
                .uri("/api/bookings/complete-by-params?username=alex&flightNumber=CA123")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.success").isEqualTo(true);
    }

    @Test
    void shouldReturnBadRequestWhenParamsBookingFails() throws Exception {
        // 给定
        CompleteBookingResponse response = CompleteBookingResponse.failure("Flight not found");

        when(bookingCoordinatorService.createCompleteBooking(anyString(), anyString()))
                .thenReturn(Mono.just(response));

        // 当 + 那么
        webTestClient.post()
                .uri("/api/bookings/complete-by-params?username=alex&flightNumber=NONEXISTENT")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.message").isEqualTo("Flight not found");
    }
}