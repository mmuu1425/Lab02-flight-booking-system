//package com.flight.gateway.service;
//
//import com.flight.gateway.dto.CompleteBookingResponse;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClientResponseException;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class BookingCoordinatorServiceTest {
//
//    @Mock
//    private WebClient webClient;
//
//    @Mock
//    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
//
//    @Mock
//    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
//
//    @Mock
//    private WebClient.ResponseSpec responseSpec;
//
//    @InjectMocks
//    private BookingCoordinatorService bookingCoordinatorService;
//
//    @Test
//    void shouldCreateCompleteBookingSuccessfully() {
//        // 给定
//        String username = "alex";
//        String flightNumber = "CA123";
//
//        String userResponse = "{\"userUid\":\"123e4567-e89b-12d3-a456-426614174000\",\"username\":\"alex\",\"name\":\"Alex Johnson\"}";
//        String flightResponse = "{\"flightUid\":\"223e4567-e89b-12d3-a456-426614174000\",\"flightNumber\":\"CA123\",\"departure\":\"Shanghai\"}";
//        String bookingResponse = "{\"bookingUid\":\"323e4567-e89b-12d3-a456-426614174000\",\"userUid\":\"123e4567-e89b-12d3-a456-426614174000\",\"flightUid\":\"223e4567-e89b-12d3-a456-426614174000\",\"status\":\"CONFIRMED\"}";
//
//        // 模拟 GET 调用
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
//
//        // 设置 GET 响应序列
//        when(responseSpec.bodyToMono(String.class))
//                .thenReturn(Mono.just(userResponse))    // 第一次调用：用户服务
//                .thenReturn(Mono.just(flightResponse))  // 第二次调用：航班服务
//                .thenReturn(Mono.just(bookingResponse)); // 第三次调用：预订服务（GET 用于验证）
//
//        // 模拟 POST 调用
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.bodyValue(any())).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
//
//        // 当
//        Mono<CompleteBookingResponse> result = bookingCoordinatorService.createCompleteBooking(username, flightNumber);
//
//        // 那么
//        StepVerifier.create(result)
//                .expectNextMatches(response ->
//                        response.isSuccess() &&
//                                response.getMessage().equals("Booking created successfully") &&
//                                response.getBookingUid().equals(UUID.fromString("323e4567-e89b-12d3-a456-426614174000"))
//                )
//                .verifyComplete();
//    }
//
//    @Test
//    void shouldHandleUserServiceError() {
//        // 给定
//        String username = "nonexistent";
//        String flightNumber = "CA123";
//
//        // 模拟 GET 调用失败
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(String.class))
//                .thenReturn(Mono.error(WebClientResponseException.create(404, "User not found", null, null, null)));
//
//        // 当
//        Mono<CompleteBookingResponse> result = bookingCoordinatorService.createCompleteBooking(username, flightNumber);
//
//        // 那么
//        StepVerifier.create(result)
//                .expectNextMatches(response ->
//                        !response.isSuccess() &&
//                                response.getMessage().contains("User service error")
//                )
//                .verifyComplete();
//    }
//
//    @Test
//    void shouldHandleFlightServiceError() {
//        // 给定
//        String username = "alex";
//        String flightNumber = "NONEXISTENT";
//
//        String userResponse = "{\"userUid\":\"123e4567-e89b-12d3-a456-426614174000\",\"username\":\"alex\",\"name\":\"Alex Johnson\"}";
//
//        // 模拟用户服务成功，航班服务失败
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
//
//        when(responseSpec.bodyToMono(String.class))
//                .thenReturn(Mono.just(userResponse))  // 第一次调用返回用户响应
//                .thenReturn(Mono.error(WebClientResponseException.create(404, "Flight not found", null, null, null))); // 第二次调用失败
//
//        // 当
//        Mono<CompleteBookingResponse> result = bookingCoordinatorService.createCompleteBooking(username, flightNumber);
//
//        // 那么
//        StepVerifier.create(result)
//                .expectNextMatches(response ->
//                        !response.isSuccess() &&
//                                response.getMessage().contains("Flight service error")
//                )
//                .verifyComplete();
//    }
//}