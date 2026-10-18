package com.vixello.tickets.services;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.UpdateEventRequest;
import com.vixello.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);
    Page<Event> listEventForOrganizer(UUID organizerId, Pageable pageable);
    Event updateEvent(UUID organizerId, UUID eventId, UpdateEventRequest eventRequest);
    void deleteEvent(UUID organizerId, UUID eventId);
}
