package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.EventDto;
import com.group.SpringMVCProject.models.Event;

import java.util.List;

public interface EventService {

    Event createEvent(Long id, EventDto eventDto);

    List<EventDto> findAllEvents();

    EventDto findEventById(Long id);

    EventDto updateEvent(EventDto eventDto);

    void deleteEvent(Long id);
}
