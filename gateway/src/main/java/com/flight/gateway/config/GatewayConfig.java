package com.flight.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${USER_SERVICE_HOST:localhost}")
    private String userServiceHost;

    @Value("${FLIGHT_SERVICE_HOST:localhost}")
    private String flightServiceHost;

    @Value("${BOOKING_SERVICE_HOST:localhost}")
    private String bookingServiceHost;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("http://" + userServiceHost + ":8050"))  // 使用环境变量
                .route("flight-service", r -> r
                        .path("/api/flights/**")
                        .uri("http://" + flightServiceHost + ":8060"))  // 使用环境变量
                .route("booking-service", r -> r
                        .path("/api/bookings/**")
                        .uri("http://" + bookingServiceHost + ":8070"))  // 使用环境变量
                .build();
    }
}