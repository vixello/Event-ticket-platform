package com.vixello.tickets.services.impl;

import com.vixello.tickets.domain.entities.Ticket;
import com.vixello.tickets.domain.entities.TicketStatusEnum;
import com.vixello.tickets.domain.entities.TicketType;
import com.vixello.tickets.domain.entities.User;
import com.vixello.tickets.exceptions.TicketSoldOutException;
import com.vixello.tickets.exceptions.TicketTypeNotFoundException;
import com.vixello.tickets.exceptions.UserNotFoundException;
import com.vixello.tickets.repositories.TicketRepository;
import com.vixello.tickets.repositories.TicketTypeRepository;
import com.vixello.tickets.repositories.UserRepository;
import com.vixello.tickets.services.QrCodeService;
import com.vixello.tickets.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final UserRepository userRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final QrCodeService qrCodeService;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with ID %s was not found", userId)
        ));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId).orElseThrow(() -> new TicketTypeNotFoundException(
                String.format("Ticket type with ID %s was not found", ticketTypeId)
        ));

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketTypeId);
        Integer totalAvailable = ticketType.getTotalAvailable();

        if(purchasedTickets + 1 > totalAvailable){
            throw new TicketSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
