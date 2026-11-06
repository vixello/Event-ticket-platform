package com.vixello.tickets.services;

import com.vixello.tickets.domain.entities.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface TicketService {
    Page<Ticket> listTicketsForUser(UUID purchaserId, Pageable pageable);
}
