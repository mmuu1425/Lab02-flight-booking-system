package com.flight.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.booking.entity.Booking;
import com.flight.booking.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID testUserUid;
    private UUID testFlightUid;

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        testUserUid = UUID.randomUUID();
        testFlightUid = UUID.randomUUID();
    }

    @Test
    void shouldReturnHealthCheck() throws Exception {
        mockMvc.perform(get("/manage/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void shouldCreateBooking() throws Exception {
        String bookingJson = String.format("""
            {
                "userUid": "%s",
                "flightUid": "%s",
                "status": "CONFIRMED"
            }
            """, testUserUid, testFlightUid);

        mockMvc.perform(post("/api/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$.flightUid").value(testFlightUid.toString()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.bookingUid").exists());
    }

    @Test
    void shouldGetBookingById() throws Exception {
        // 先创建预订
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        mockMvc.perform(get("/api/bookings/" + savedBooking.getBookingUid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$.flightUid").value(testFlightUid.toString()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.bookingUid").value(savedBooking.getBookingUid().toString()));
    }

    @Test
    void shouldReturnNotFoundForNonExistingBooking() throws Exception {
        UUID nonExistingUid = UUID.randomUUID();
        mockMvc.perform(get("/api/bookings/" + nonExistingUid))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetAllBookings() throws Exception {
        // 创建多个预订
        bookingRepository.save(new Booking(testUserUid, testFlightUid, "CONFIRMED"));
        bookingRepository.save(new Booking(UUID.randomUUID(), UUID.randomUUID(), "PENDING"));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].status").exists())
                .andExpect(jsonPath("$[1].status").exists());
    }

    @Test
    void shouldGetBookingsByUser() throws Exception {
        // 创建测试数据
        UUID otherUserUid = UUID.randomUUID();
        bookingRepository.save(new Booking(testUserUid, testFlightUid, "CONFIRMED"));
        bookingRepository.save(new Booking(testUserUid, UUID.randomUUID(), "CANCELLED"));
        bookingRepository.save(new Booking(otherUserUid, testFlightUid, "PENDING")); // 其他用户的预订

        mockMvc.perform(get("/api/bookings/user/" + testUserUid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$[1].userUid").value(testUserUid.toString()));
    }

    @Test
    void shouldGetBookingsByFlight() throws Exception {
        // 创建测试数据
        UUID otherFlightUid = UUID.randomUUID();
        bookingRepository.save(new Booking(testUserUid, testFlightUid, "CONFIRMED"));
        bookingRepository.save(new Booking(UUID.randomUUID(), testFlightUid, "PENDING"));
        bookingRepository.save(new Booking(testUserUid, otherFlightUid, "CANCELLED")); // 其他航班的预订

        mockMvc.perform(get("/api/bookings/flight/" + testFlightUid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].flightUid").value(testFlightUid.toString()))
                .andExpect(jsonPath("$[1].flightUid").value(testFlightUid.toString()));
    }

    @Test
    void shouldUpdateBookingStatus() throws Exception {
        // 先创建预订
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        mockMvc.perform(put("/api/bookings/" + savedBooking.getBookingUid() + "/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$.flightUid").value(testFlightUid.toString()));
    }

    @Test
    void shouldCancelBooking() throws Exception {
        // 先创建预订
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        mockMvc.perform(patch("/api/bookings/" + savedBooking.getBookingUid() + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$.flightUid").value(testFlightUid.toString()));
    }

    @Test
    void shouldUpdateBooking() throws Exception {
        // 先创建预订
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        String updateJson = """
            {
                "status": "RESCHEDULED"
            }
            """;

        mockMvc.perform(put("/api/bookings/" + savedBooking.getBookingUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("RESCHEDULED"))
                .andExpect(jsonPath("$.userUid").value(testUserUid.toString()))
                .andExpect(jsonPath("$.flightUid").value(testFlightUid.toString()));
    }

    @Test
    void shouldDeleteBooking() throws Exception {
        // 先创建预订
        Booking booking = new Booking(testUserUid, testFlightUid, "CONFIRMED");
        Booking savedBooking = bookingRepository.save(booking);

        mockMvc.perform(delete("/api/bookings/" + savedBooking.getBookingUid()))
                .andExpect(status().isOk())
                .andExpect(content().string("Booking deleted successfully"));

        // 验证预订确实被删除
        mockMvc.perform(get("/api/bookings/" + savedBooking.getBookingUid()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEmptyListForUserWithNoBookings() throws Exception {
        UUID userWithNoBookings = UUID.randomUUID();
        mockMvc.perform(get("/api/bookings/user/" + userWithNoBookings))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnEmptyListForFlightWithNoBookings() throws Exception {
        UUID flightWithNoBookings = UUID.randomUUID();
        mockMvc.perform(get("/api/bookings/flight/" + flightWithNoBookings))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}