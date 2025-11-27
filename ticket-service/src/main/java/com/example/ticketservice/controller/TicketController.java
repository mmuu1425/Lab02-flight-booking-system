package com.example.ticketservice.controller;

import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // 获取用户所有票
    @GetMapping
    public ResponseEntity<List<Ticket>> getUserTickets(@RequestHeader("X-User-Name") String username) {
        List<Ticket> tickets = ticketService.getUserTickets(username);
        return ResponseEntity.ok(tickets);
    }

    // 获取特定票信息 - 修复：明确指定参数名
    @GetMapping("/{ticketUid}")
    public ResponseEntity<Ticket> getTicket(
            @PathVariable("ticketUid") UUID ticketUid,  // 明确指定参数名
            @RequestHeader("X-User-Name") String username) {

        try {
            Optional<Ticket> ticket = ticketService.getUserTicket(username, ticketUid);
            return ticket.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 购买票
    @PostMapping
    public ResponseEntity<TicketResponse> purchaseTicket(
            @RequestBody TicketPurchaseRequest request,
            @RequestHeader("X-User-Name") String username) {

        Ticket ticket = ticketService.createTicket(username, request.getFlightNumber(), request.getPrice());

        TicketResponse response = new TicketResponse(
                ticket.getTicketUid(),
                ticket.getFlightNumber(),
                ticket.getPrice(),
                ticket.getStatus()
        );

        return ResponseEntity.ok(response);
    }

    // 退票 - 修复：明确指定参数名
    @DeleteMapping("/{ticketUid}")
    public ResponseEntity<Void> cancelTicket(
            @PathVariable("ticketUid") UUID ticketUid,  // 明确指定参数名
            @RequestHeader("X-User-Name") String username) {

        try {
            boolean canceled = ticketService.cancelTicket(username, ticketUid);
            return canceled ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // 健康检查
    @GetMapping("/manage/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    // DTO类
    public static class TicketPurchaseRequest {
        private String flightNumber;
        private Integer price;
        private Boolean paidFromBalance;

        public String getFlightNumber() { return flightNumber; }
        public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

        public Integer getPrice() { return price; }
        public void setPrice(Integer price) { this.price = price; }

        public Boolean getPaidFromBalance() { return paidFromBalance; }
        public void setPaidFromBalance(Boolean paidFromBalance) { this.paidFromBalance = paidFromBalance; }
    }

    public static class TicketResponse {
        private final UUID ticketUid;
        private final String flightNumber;
        private final Integer price;
        private final String status;

        public TicketResponse(UUID ticketUid, String flightNumber, Integer price, String status) {
            this.ticketUid = ticketUid;
            this.flightNumber = flightNumber;
            this.price = price;
            this.status = status;
        }

        public UUID getTicketUid() { return ticketUid; }
        public String getFlightNumber() { return flightNumber; }
        public Integer getPrice() { return price; }
        public String getStatus() { return status; }
    }
}