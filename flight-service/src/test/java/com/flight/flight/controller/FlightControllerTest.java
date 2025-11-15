package com.flight.flight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.flight.dto.FlightDTO;
import com.flight.flight.entity.Flight;
import com.flight.flight.service.FlightService;
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

@WebMvcTest(FlightController.class)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateFlight() throws Exception {
        // 给定
        Flight flight = new Flight("CA123", "Shanghai", "Beijing", 150);
        FlightDTO flightDTO = new FlightDTO(UUID.randomUUID(), "CA123", "Shanghai", "Beijing", 150);

        when(flightService.createFlight(any(Flight.class))).thenReturn(flightDTO);

        // 当 + 那么
        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("CA123"))
                .andExpect(jsonPath("$.departure").value("Shanghai"))
                .andExpect(jsonPath("$.destination").value("Beijing"))
                .andExpect(jsonPath("$.availableSeats").value(150));
    }

    @Test
    void shouldGetFlightByNumber() throws Exception {
        // 给定
        FlightDTO flightDTO = new FlightDTO(UUID.randomUUID(), "CA123", "Shanghai", "Beijing", 150);
        when(flightService.getFlightByNumber("CA123")).thenReturn(flightDTO);

        // 当 + 那么
        mockMvc.perform(get("/api/flights/number/CA123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value("CA123"))
                .andExpect(jsonPath("$.departure").value("Shanghai"));
    }

    @Test
    void shouldGetAllFlights() throws Exception {
        // 给定
        FlightDTO flight1 = new FlightDTO(UUID.randomUUID(), "CA123", "Shanghai", "Beijing", 150);
        FlightDTO flight2 = new FlightDTO(UUID.randomUUID(), "MU456", "Beijing", "Shanghai", 200);
        List<FlightDTO> flights = Arrays.asList(flight1, flight2);

        when(flightService.getAllFlights()).thenReturn(flights);

        // 当 + 那么
        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].flightNumber").value("CA123"))
                .andExpect(jsonPath("$[1].flightNumber").value("MU456"));
    }

    @Test
    void shouldSearchFlights() throws Exception {
        // 给定
        FlightDTO flight = new FlightDTO(UUID.randomUUID(), "CA123", "Shanghai", "Beijing", 150);
        List<FlightDTO> flights = Arrays.asList(flight);

        when(flightService.searchFlights("Shanghai", "Beijing")).thenReturn(flights);

        // 当 + 那么
        mockMvc.perform(get("/api/flights/search")
                        .param("from", "Shanghai")
                        .param("to", "Beijing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].flightNumber").value("CA123"));
    }

    @Test
    void shouldUpdateFlight() throws Exception {
        // 给定
        UUID flightUid = UUID.randomUUID();
        Flight flightDetails = new Flight();
        flightDetails.setDeparture("Shanghai Pudong");
        flightDetails.setDestination("Beijing Capital");
        flightDetails.setAvailableSeats(180);

        FlightDTO updatedFlight = new FlightDTO(flightUid, "CA123", "Shanghai Pudong", "Beijing Capital", 180);
        when(flightService.updateFlight(eq(flightUid), any(Flight.class))).thenReturn(updatedFlight);

        // 当 + 那么
        mockMvc.perform(put("/api/flights/" + flightUid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flightDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departure").value("Shanghai Pudong"))
                .andExpect(jsonPath("$.destination").value("Beijing Capital"))
                .andExpect(jsonPath("$.availableSeats").value(180));
    }

    @Test
    void shouldUpdateSeats() throws Exception {
        // 给定
        UUID flightUid = UUID.randomUUID();
        FlightDTO updatedFlight = new FlightDTO(flightUid, "CA123", "Shanghai", "Beijing", 120);
        when(flightService.updateSeats(flightUid, 120)).thenReturn(updatedFlight);

        // 当 + 那么
        mockMvc.perform(patch("/api/flights/" + flightUid + "/seats")
                        .param("seats", "120"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSeats").value(120));
    }

    @Test
    void shouldReduceSeats() throws Exception {
        // 给定
        UUID flightUid = UUID.randomUUID();
        FlightDTO updatedFlight = new FlightDTO(flightUid, "CA123", "Shanghai", "Beijing", 147);
        when(flightService.reduceSeats(flightUid, 3)).thenReturn(updatedFlight);

        // 当 + 那么
        mockMvc.perform(patch("/api/flights/" + flightUid + "/reduce-seats")
                        .param("count", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSeats").value(147));
    }

    @Test
    void shouldAddSeats() throws Exception {
        // 给定
        UUID flightUid = UUID.randomUUID();
        FlightDTO updatedFlight = new FlightDTO(flightUid, "CA123", "Shanghai", "Beijing", 160);
        when(flightService.addSeats(flightUid, 10)).thenReturn(updatedFlight);

        // 当 + 那么
        mockMvc.perform(patch("/api/flights/" + flightUid + "/add-seats")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSeats").value(160));
    }

    @Test
    void shouldDeleteFlight() throws Exception {
        // 给定
        UUID flightUid = UUID.randomUUID();
        doNothing().when(flightService).deleteFlight(flightUid);

        // 当 + 那么
        mockMvc.perform(delete("/api/flights/" + flightUid))
                .andExpect(status().isOk())
                .andExpect(content().string("Flight deleted successfully"));

        verify(flightService).deleteFlight(flightUid);
    }
}