package com.flight.flight;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.flight.entity.Flight;
import com.flight.flight.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FlightApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();
    }

    @Test
    void shouldReturnHealthCheck() throws Exception {
        mockMvc.perform(get("/manage/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void shouldCreateFlight() throws Exception {
        String flightJson = """
            {
                "flightNumber": "CA123",
                "departure": "Shanghai",
                "destination": "Beijing",
                "availableSeats": 150
            }
            """;

        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(flightJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("CA123"))
                .andExpect(jsonPath("$.departure").value("Shanghai"))
                .andExpect(jsonPath("$.destination").value("Beijing"))
                .andExpect(jsonPath("$.availableSeats").value(150))
                .andExpect(jsonPath("$.flightUid").exists());
    }

    @Test
    void shouldGetFlightByFlightNumber() throws Exception {
        // 先创建航班
        Flight flight = new Flight("MU456", "Beijing", "Shanghai", 200);
        Flight savedFlight = flightRepository.save(flight);

        mockMvc.perform(get("/api/flights/number/MU456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("MU456"))
                .andExpect(jsonPath("$.departure").value("Beijing"))
                .andExpect(jsonPath("$.destination").value("Shanghai"))
                .andExpect(jsonPath("$.availableSeats").value(200));
    }

    @Test
    void shouldReturnNotFoundForNonExistingFlightNumber() throws Exception {
        mockMvc.perform(get("/api/flights/number/NONEXISTENT"))
                .andExpect(status().isNotFound()); // 改为 404
    }

    @Test
    void shouldGetFlightById() throws Exception {
        // 先创建航班
        Flight flight = new Flight("CZ789", "Guangzhou", "Beijing", 180);
        Flight savedFlight = flightRepository.save(flight);

        mockMvc.perform(get("/api/flights/" + savedFlight.getFlightUid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("CZ789"))
                .andExpect(jsonPath("$.flightUid").value(savedFlight.getFlightUid().toString()));
    }

    @Test
    void shouldGetAllFlights() throws Exception {
        // 创建多个航班
        flightRepository.save(new Flight("CA123", "Shanghai", "Beijing", 150));
        flightRepository.save(new Flight("MU456", "Beijing", "Shanghai", 200));

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].flightNumber").exists())
                .andExpect(jsonPath("$[1].flightNumber").exists());
    }

    @Test
    void shouldSearchFlights() throws Exception {
        // 创建测试数据
        flightRepository.save(new Flight("CA123", "Shanghai", "Beijing", 150));
        flightRepository.save(new Flight("MU456", "Beijing", "Shanghai", 200));
        flightRepository.save(new Flight("CZ789", "Shanghai", "Guangzhou", 180));

        mockMvc.perform(get("/api/flights/search")
                        .param("from", "Shanghai")
                        .param("to", "Beijing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].flightNumber").value("CA123"));
    }

    @Test
    void shouldUpdateFlight() throws Exception {
        // 先创建航班
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        Flight savedFlight = flightRepository.save(flight);

        String updateJson = """
            {
                "flightNumber": "CA123",
                "departure": "Shanghai Pudong",
                "destination": "Beijing Capital",
                "availableSeats": 180
            }
            """;

        mockMvc.perform(put("/api/flights/" + savedFlight.getFlightUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departure").value("Shanghai Pudong"))
                .andExpect(jsonPath("$.destination").value("Beijing Capital"))
                .andExpect(jsonPath("$.availableSeats").value(180));
    }

    @Test
    void shouldUpdateSeats() throws Exception {
        // 先创建航班
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        Flight savedFlight = flightRepository.save(flight);

        mockMvc.perform(patch("/api/flights/" + savedFlight.getFlightUid() + "/seats")
                        .param("seats", "120"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSeats").value(120));
    }

    @Test
    void shouldReduceSeats() throws Exception {
        // 先创建航班
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        Flight savedFlight = flightRepository.save(flight);

        mockMvc.perform(patch("/api/flights/" + savedFlight.getFlightUid() + "/reduce-seats")
                        .param("count", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSeats").value(147));
    }

    @Test
    void shouldNotReduceSeatsBelowZero() throws Exception {
        // 先创建航班（只有5个座位）
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 5);
        Flight savedFlight = flightRepository.save(flight);

        mockMvc.perform(patch("/api/flights/" + savedFlight.getFlightUid() + "/reduce-seats")
                        .param("count", "10")) // 尝试减少10个座位
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAddSeats() throws Exception {
        // 先创建航班
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        Flight savedFlight = flightRepository.save(flight);

        mockMvc.perform(patch("/api/flights/" + savedFlight.getFlightUid() + "/add-seats")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSeats").value(160));
    }

    @Test
    void shouldDeleteFlight() throws Exception {
        // 先创建航班
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        Flight savedFlight = flightRepository.save(flight);

        mockMvc.perform(delete("/api/flights/" + savedFlight.getFlightUid()))
                .andExpect(status().isOk())
                .andExpect(content().string("Flight deleted successfully"));

        // 验证航班确实被删除
        mockMvc.perform(get("/api/flights/" + savedFlight.getFlightUid()))
                .andExpect(status().isNotFound());
    }
}