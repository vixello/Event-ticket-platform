package com.vixello.tickets.services;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.UpdateEventRequest;
import com.vixello.tickets.domain.entities.Event;

import java.util.UUID;

public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest eventRequest);
    Event updateEvent(UUID organizerId, UUID eventId, UpdateEventRequest eventRequest);
    void deleteEvent(UUID organizerId, UUID eventId);
}
