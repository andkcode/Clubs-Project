package com.group.SpringMVCProject.service.impl;

import com.group.SpringMVCProject.dto.EventDto;
import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.models.Event;
import com.group.SpringMVCProject.repository.ClubRepository;
import com.group.SpringMVCProject.repository.EventRepository;
import com.group.SpringMVCProject.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.group.SpringMVCProject.mapper.EventMapper.mapToEvent;
import static com.group.SpringMVCProject.mapper.EventMapper.mapToEventDto;

@Service
public class EventServiceImpl implements EventService {
    private EventRepository eventRepository;
    private ClubRepository clubRepository;

    @Autowired
    public EventServiceImpl(ClubRepository clubRepository, EventRepository eventRepository) {
        this.clubRepository = clubRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(Long id, EventDto eventDto) {
        Club club = clubRepository.findById(id).get();
        Event event = mapToEvent(eventDto);
        event.setClub(club);
        eventRepository.save(event);
        return event;
    }

    @Override
    public List<EventDto> findAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(event -> mapToEventDto(event)).collect(Collectors.toList());
    }

    @Override
    public EventDto findEventById(Long id) {
        Event event = eventRepository.findById(id).get();
        return mapToEventDto(event);
    }

    @Override
    public EventDto updateEvent(EventDto eventDto) {
        Event event = mapToEvent(eventDto);
        eventRepository.save(event);
        return eventDto;
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
