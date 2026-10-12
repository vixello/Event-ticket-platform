package com.vixello.tickets.services.impl;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.UpdateEventRequest;
import com.vixello.tickets.domain.entities.Event;
import com.vixello.tickets.domain.entities.TicketType;
import com.vixello.tickets.domain.entities.User;
import com.vixello.tickets.exceptions.UserNotFoundException;
import com.vixello.tickets.repositories.EventRepository;
import com.vixello.tickets.repositories.UserRepository;
import com.vixello.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest eventRequest) {
        User organizer = userRepository.findById(organizerId).orElseThrow(() ->
                new UserNotFoundException(String.format("User with ID '%s' not found", organizerId)));

        List<TicketType> ticketTypes = eventRequest.getTicketTypes().stream().map(ticketType ->
        {
            TicketType ticketTypeToCreate = new TicketType();
            ticketTypeToCreate.setName(ticketType.getName());
            ticketTypeToCreate.setDescription(ticketType.getDescription());
            ticketTypeToCreate.setPrice(ticketType.getPrice());
            ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());

            return ticketTypeToCreate;
        }).toList();

        Event eventToCreate = new Event();
        eventToCreate.setName(eventRequest.getName());
        eventToCreate.setStart(eventRequest.getStart());
        eventToCreate.setEnd(eventRequest.getEnd());
        eventToCreate.setVenue(eventRequest.getVenue());
        eventToCreate.setSaleStart(eventRequest.getSaleStart());
        eventToCreate.setSaleEnd(eventRequest.getSaleEnd());
        eventToCreate.setStatus(eventRequest.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypes);

        return eventRepository.save(eventToCreate);
    }

    @Override
    public Event updateEvent(UUID organizerId, UUID eventId, UpdateEventRequest eventRequest) {
        return null;
    }

    @Override
    public void deleteEvent(UUID organizerId, UUID eventId) {

    }
}
