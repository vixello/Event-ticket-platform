package com.vixello.tickets.domain.controllers;

import com.vixello.tickets.domain.dtos.ListTicketResponseDto;
import com.vixello.tickets.domain.mappers.TicketMapper;
import com.vixello.tickets.services.TicketService;
import com.vixello.tickets.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @GetMapping
    public Page<ListTicketResponseDto> listTickets(
            @AuthenticationPrincipal Jwt jwt,
            Pageable pageable
            ){
        UUID userId = JwtUtil.parseUserId(jwt);

        return ticketService.listTicketsForUser(userId, pageable)
                            .map(ticketMapper::toListTicketResponseDto);
    }
}
