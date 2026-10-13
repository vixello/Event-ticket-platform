package com.vixello.tickets.domain.mappers;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.CreateTicketTypeRequest;
import com.vixello.tickets.domain.dtos.CreateEventRequestDto;
import com.vixello.tickets.domain.dtos.CreateEventResponseDto;
import com.vixello.tickets.domain.dtos.CreateTicketTypeResponseDto;
import com.vixello.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {
    CreateTicketTypeRequest fromDto(CreateTicketTypeResponseDto dto);
    CreateTicketTypeResponseDto toDto(CreateTicketTypeRequest typeRequest);
    CreateEventRequest fromDto(CreateEventRequestDto dto);
    CreateEventResponseDto toDto(Event event);
}
