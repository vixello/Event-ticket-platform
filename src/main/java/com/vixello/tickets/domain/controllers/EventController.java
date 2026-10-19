package com.vixello.tickets.domain.controllers;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.dtos.CreateEventRequestDto;
import com.vixello.tickets.domain.dtos.CreateEventResponseDto;
import com.vixello.tickets.domain.dtos.ListEventResponseDto;
import com.vixello.tickets.domain.entities.Event;
import com.vixello.tickets.domain.mappers.EventMapper;
import com.vixello.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
public class EventController {
    private final EventMapper eventMapper;
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto
            ){
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID userId = parseUserId(jwt);

        Event createdEvent = eventService.createEvent(userId, createEventRequest);
        CreateEventResponseDto createdEventResponseDto = eventMapper.toDto(createdEvent);
        return new ResponseEntity<>(createdEventResponseDto, HttpStatus.CREATED);
    }

    private static UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(@AuthenticationPrincipal Jwt jwt, Pageable pageable){
        UUID organizerId = parseUserId(jwt);

        Page<Event> events = eventService.listEventForOrganizer(organizerId, pageable);
        return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));
    }
}
