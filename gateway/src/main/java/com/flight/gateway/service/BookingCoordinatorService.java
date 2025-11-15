package com.flight.gateway.service;

import com.flight.gateway.dto.CompleteBookingResponse;
import com.flight.gateway.exception.CoordinationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BookingCoordinatorService {

    private final WebClient webClient;

    @Autowired
    public BookingCoordinatorService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<CompleteBookingResponse> createCompleteBooking(String username, String flightNumber) {
        return getUserByUsername(username)
                .flatMap(userResponse -> {
                    UUID userUid = extractUserUid(userResponse);

                    return getFlightByNumber(flightNumber)
                            .flatMap(flightResponse -> {
                                UUID flightUid = extractFlightUid(flightResponse);

                                return createBooking(userUid, flightUid)
                                        .map(bookingResponse -> {
                                            UUID bookingUid = extractBookingUid(bookingResponse);
                                            return CompleteBookingResponse.success(bookingUid, userUid, flightUid);
                                        });
                            })
                            .onErrorResume(flightError -> {
                                String errorMessage = "Flight service error: " + getErrorMessage(flightError);
                                return Mono.just(CompleteBookingResponse.failure(errorMessage));
                            });
                })
                .onErrorResume(userError -> {
                    String errorMessage = "User service error: " + getErrorMessage(userError);
                    return Mono.just(CompleteBookingResponse.failure(errorMessage));
                })
                .onErrorResume(bookingError -> {
                    String errorMessage = "Booking service error: " + getErrorMessage(bookingError);
                    return Mono.just(CompleteBookingResponse.failure(errorMessage));
                });
    }

    // 调用用户服务获取用户信息
    private Mono<String> getUserByUsername(String username) {
        return webClient.get()
                .uri("http://localhost:8050/api/users/username/{username}", username)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class,
                        ex -> new CoordinationException("User service unavailable", ex));
    }

    // 调用航班服务获取航班信息
    private Mono<String> getFlightByNumber(String flightNumber) {
        return webClient.get()
                .uri("http://localhost:8060/api/flights/number/{flightNumber}", flightNumber)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class,
                        ex -> new CoordinationException("Flight service unavailable", ex));
    }

    // 调用预订服务创建预订
    private Mono<String> createBooking(UUID userUid, UUID flightUid) {
        String bookingJson = String.format(
                "{\"userUid\": \"%s\", \"flightUid\": \"%s\", \"status\": \"CONFIRMED\"}",
                userUid, flightUid);

        return webClient.post()
                .uri("http://localhost:8070/api/bookings")
                .header("Content-Type", "application/json")
                .bodyValue(bookingJson)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(WebClientResponseException.class,
                        ex -> new CoordinationException("Booking service unavailable", ex));
    }

    // 从用户响应中提取 userUid
    private UUID extractUserUid(String userResponse) {
        try {
            String userUidStr = userResponse.split("\"userUid\":\"")[1].split("\"")[0];
            return UUID.fromString(userUidStr);
        } catch (Exception e) {
            throw new CoordinationException("Failed to parse user response: " + userResponse);
        }
    }

    // 从航班响应中提取 flightUid
    private UUID extractFlightUid(String flightResponse) {
        try {
            String flightUidStr = flightResponse.split("\"flightUid\":\"")[1].split("\"")[0];
            return UUID.fromString(flightUidStr);
        } catch (Exception e) {
            throw new CoordinationException("Failed to parse flight response: " + flightResponse);
        }
    }

    // 从预订响应中提取 bookingUid
    private UUID extractBookingUid(String bookingResponse) {
        try {
            String bookingUidStr = bookingResponse.split("\"bookingUid\":\"")[1].split("\"")[0];
            return UUID.fromString(bookingUidStr);
        } catch (Exception e) {
            throw new CoordinationException("Failed to parse booking response: " + bookingResponse);
        }
    }

    // 获取错误信息
    private String getErrorMessage(Throwable error) {
        if (error instanceof WebClientResponseException) {
            WebClientResponseException webEx = (WebClientResponseException) error;
            return String.format("Status %d: %s", webEx.getStatusCode().value(), webEx.getStatusText());
        }
        return error.getMessage();
    }
}