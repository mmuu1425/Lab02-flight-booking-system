package com.flight.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/users/**")
                        .uri("http://localhost:8050"))
                .route("flight-service", r -> r
                        .path("/api/flights/**")
                        .uri("http://localhost:8060"))
                .route("booking-service", r -> r
                        .path("/api/bookings/**")
                        .uri("http://localhost:8070"))
                .build();
    }
}