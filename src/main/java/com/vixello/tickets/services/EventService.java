package com.vixello.tickets.services;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.UpdateEventRequest;
import com.vixello.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);

    Page<Event> listEventForOrganizer(UUID organizerId, Pageable pageable);
    Optional<Event> getEventByOrganizer(UUID organizerId, UUID event);
    Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest eventRequest);
    void deleteEventForOrganizer(UUID organizerId, UUID eventId);

    Page<Event> listPublishedEvents(Pageable pageable);
}
