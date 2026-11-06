package com.vixello.tickets.domain.mappers;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.CreateTicketTypeRequest;
import com.vixello.tickets.domain.UpdateEventRequest;
import com.vixello.tickets.domain.UpdateTicketTypeRequest;
import com.vixello.tickets.domain.dtos.*;
import com.vixello.tickets.domain.entities.Event;
import com.vixello.tickets.domain.entities.Ticket;
import com.vixello.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromDto(CreateTicketTypeResponseDto dto);
    CreateTicketTypeResponseDto toDto(CreateTicketTypeRequest typeRequest);
    CreateEventRequest fromDto(CreateEventRequestDto dto);
    CreateEventResponseDto toDto(Event event);

    ListEventResponseDto toListEventResponseDto(Event event);
    ListTicketTypeResponseDto toDto(TicketType ticketType);

    GetEventDetailsResponseDto toGetEventDetailsResponseDto(Event event);
    GetEventDetailsTicketTypeResponseDto toGetEventTicketTypeResponseDto(TicketType ticketType);

    UpdateTicketTypeRequest fromDto(UpdateTicketTypeRequestDto dto);
    UpdateEventRequest fromDto(UpdateEventRequestDto dto);
    UpdateTicketTypeResponseDto toUpdateTicketTypeResponseDto(TicketType ticketType);
    UpdateEventResponseDto toUpdateEventResponseDto(Event event);

    ListPublishedEventResponseDto toListPublishedEventResponseDto(Event event);
    GetPublishedEventDetailsResponseDto toGetPublishedEventDetailsResponseDto(Event event);
    GetPublishedEventDetailsTicketTypeResponseDto toGetPublishedEventDetailsTicketTypeResponseDto(TicketType ticketType);
}
