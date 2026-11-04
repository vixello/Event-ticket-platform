package com.vixello.tickets.domain.controllers;

import com.vixello.tickets.domain.dtos.GetPublishedEventDetailsResponseDto;
import com.vixello.tickets.domain.dtos.ListPublishedEventResponseDto;
import com.vixello.tickets.domain.entities.Event;
import com.vixello.tickets.domain.mappers.EventMapper;
import com.vixello.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> getPublishedEvents(
            @RequestParam(required = false, name = "searchTerm") String searchTerm,
            Pageable pageable){

        Page<Event> events = null;
        if(searchTerm != null  && !searchTerm.trim().isEmpty()){
            events = eventService.searchPublishedEvents(searchTerm, pageable);
        }
        else{
            events = eventService.listPublishedEvents(pageable);
        }

       return ResponseEntity.ok(events.map(eventMapper::toListPublishedEventResponseDto));
    }

    @GetMapping(path="/{eventId}")
    public ResponseEntity<GetPublishedEventDetailsResponseDto> getPublishedEvent(
            @PathVariable UUID eventId)
    {
        return eventService.getPublishedEvent(eventId)
                .map(eventMapper::toGetPublishedEventDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
