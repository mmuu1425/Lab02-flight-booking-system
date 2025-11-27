package com.example.ticketservice.service;

import com.example.ticketservice.entity.Ticket;
import com.example.ticketservice.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // 获取用户所有票
    public List<Ticket> getUserTickets(String username) {
        return ticketRepository.findByUsernameOrderByIdDesc(username);
    }

    // 获取用户特定票
    public Optional<Ticket> getUserTicket(String username, UUID ticketUid) {
        return ticketRepository.findByTicketUidAndUsername(ticketUid, username);
    }

    // 创建新票
    @Transactional
    public Ticket createTicket(String username, String flightNumber, Integer price) {
        Ticket ticket = new Ticket(username, flightNumber, price, "PAID");
        return ticketRepository.save(ticket);
    }

    // 退票（标记为取消）
    @Transactional
    public boolean cancelTicket(String username, UUID ticketUid) {
        Optional<Ticket> ticketOpt = ticketRepository.findByTicketUidAndUsername(ticketUid, username);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setStatus("CANCELED");
            ticketRepository.save(ticket);
            return true;
        }
        return false;
    }

    // 检查票是否存在
    public boolean ticketExists(UUID ticketUid) {
        return ticketRepository.existsByTicketUid(ticketUid);
    }
}