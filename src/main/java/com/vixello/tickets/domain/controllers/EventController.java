package com.vixello.tickets.domain.controllers;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.UpdateEventRequest;
import com.vixello.tickets.domain.dtos.*;
import com.vixello.tickets.domain.entities.Event;
import com.vixello.tickets.domain.mappers.EventMapper;
import com.vixello.tickets.services.EventService;
import com.vixello.tickets.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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
        UUID userId = JwtUtil.parseUserId(jwt);

        Event createdEvent = eventService.createEvent(userId, createEventRequest);
        CreateEventResponseDto createdEventResponseDto = eventMapper.toDto(createdEvent);
        return new ResponseEntity<>(createdEventResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(@AuthenticationPrincipal Jwt jwt, Pageable pageable){
        UUID organizerId = JwtUtil.parseUserId(jwt);

        Page<Event> events = eventService.listEventForOrganizer(organizerId, pageable);
        return ResponseEntity.ok(events.map(eventMapper::toListEventResponseDto));
    }

    @GetMapping(path = "/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId){
        UUID userId = JwtUtil.parseUserId(jwt);

        return eventService.getEventByOrganizer(userId, eventId)
                .map(eventMapper::toGetEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(@AuthenticationPrincipal Jwt jwt,
                                                              @PathVariable UUID eventId,
                                                              @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto
                                                              ){
        UUID userId = JwtUtil.parseUserId(jwt);

        UpdateEventRequest updateEventRequest = eventMapper.fromDto(updateEventRequestDto);
        Event updatedEvent = eventService.updateEventForOrganizer(userId, eventId, updateEventRequest);

        UpdateEventResponseDto updateEventResponseDto = eventMapper.toUpdateEventResponseDto(updatedEvent);

        return ResponseEntity.ok(updateEventResponseDto);
    }

    @DeleteMapping(path = "/{eventId}")
    public ResponseEntity deleteEvent(@AuthenticationPrincipal Jwt jwt, @PathVariable UUID eventId){
        UUID userId = JwtUtil.parseUserId(jwt);

        eventService.deleteEventForOrganizer(userId, eventId);
        return ResponseEntity.noContent().build();
    }
}
