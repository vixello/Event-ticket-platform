package com.vixello.tickets.services.impl;

import com.vixello.tickets.domain.CreateEventRequest;
import com.vixello.tickets.domain.UpdateEventRequest;
import com.vixello.tickets.domain.UpdateTicketTypeRequest;
import com.vixello.tickets.domain.entities.Event;
import com.vixello.tickets.domain.entities.TicketType;
import com.vixello.tickets.domain.entities.User;
import com.vixello.tickets.exceptions.EventNotFoundException;
import com.vixello.tickets.exceptions.EventUpdateException;
import com.vixello.tickets.exceptions.TicketTypeNotFoundException;
import com.vixello.tickets.exceptions.UserNotFoundException;
import com.vixello.tickets.repositories.EventRepository;
import com.vixello.tickets.repositories.UserRepository;
import com.vixello.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Event createEvent(UUID organizerId, CreateEventRequest eventRequest) {
        User organizer = userRepository.findById(organizerId).orElseThrow(() ->
                new UserNotFoundException(String.format("User with ID '%s' not found", organizerId)));

        Event eventToCreate = new Event();

        List<TicketType> ticketTypes = eventRequest.getTicketTypes().stream().map(ticketType ->
        {
            TicketType ticketTypeToCreate = new TicketType();
            ticketTypeToCreate.setName(ticketType.getName());
            ticketTypeToCreate.setDescription(ticketType.getDescription());
            ticketTypeToCreate.setPrice(ticketType.getPrice());
            ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
            ticketTypeToCreate.setEvent(eventToCreate);
            return ticketTypeToCreate;
        }).toList();

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
    public Page<Event> listEventForOrganizer(UUID organizerId, Pageable pageable){
        User organizer = userRepository.findById(organizerId).orElseThrow(() ->
                new UserNotFoundException(String.format("User with ID '%s' not found", organizerId)));

        return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventByOrganizer(UUID organizerId, UUID event) {
        return eventRepository.findByIdAndOrganizerId(event, organizerId);
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, UUID eventId, UpdateEventRequest updateEvent) {
        if(null == updateEvent.getId()){
            throw new EventUpdateException("Event ID cannot be null");
        }

        if(!eventId.equals(updateEvent.getId())){
            throw new EventUpdateException("IDs do not match. Cannot update the ID of an event");
        }

        Event existingEvent = eventRepository.findByIdAndOrganizerId(eventId, organizerId).orElseThrow(() ->
                new EventNotFoundException(String.format("Event with ID '%s' not found", eventId)));

        existingEvent.setName(updateEvent.getName());
        existingEvent.setStart(updateEvent.getStart());
        existingEvent.setEnd(updateEvent.getEnd());
        existingEvent.setVenue(updateEvent.getVenue());
        existingEvent.setSaleStart(updateEvent.getSaleStart());
        existingEvent.setSaleEnd(updateEvent.getSaleEnd());
        existingEvent.setStatus(updateEvent.getStatus());

        Set<UUID> requestTicketTypeIds = updateEvent.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes().removeIf(existingTicketType ->
                !requestTicketTypeIds.contains(existingTicketType.getId()));

        Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for(UpdateTicketTypeRequest ticketType : updateEvent.getTicketTypes()){
            if(null == ticketType.getId()){
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent);
                existingEvent.getTicketTypes().add(ticketTypeToCreate);

            } else if (existingTicketTypesIndex.containsKey(ticketType.getId())){
                TicketType existingTicketType = existingTicketTypesIndex.get(ticketType.getId());
                existingTicketType.setName(ticketType.getName());
                existingTicketType.setDescription(ticketType.getDescription());
                existingTicketType.setPrice(ticketType.getPrice());
                existingTicketType.setTotalAvailable(ticketType.getTotalAvailable());
            } else {
                throw new TicketTypeNotFoundException(String.format(
                        "Ticket type with ID '%s' does not exist", ticketType.getId()
                ));
            }
        }

        return eventRepository.save(existingEvent);
    }

    @Override
    public void deleteEvent(UUID organizerId, UUID eventId) {

    }
}
