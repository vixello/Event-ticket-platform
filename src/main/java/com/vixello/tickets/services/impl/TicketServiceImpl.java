package com.vixello.tickets.services.impl;

import com.vixello.tickets.domain.entities.Ticket;
import com.vixello.tickets.repositories.TicketRepository;
import com.vixello.tickets.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Override
    public Page<Ticket> listTicketsForUser(UUID purchaserId, Pageable pageable) {
        return ticketRepository.findByPurchaserId(purchaserId, pageable);
    }
}
