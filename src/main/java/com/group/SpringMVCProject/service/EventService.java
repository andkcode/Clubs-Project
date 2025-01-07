package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.EventDto;

import java.util.List;

public interface EventService {

    void createEvent(Long clubId, EventDto eventDto);

    List<EventDto> findAllEvents();

    EventDto findEventById(Long eventId);

    void updateEvent(EventDto eventDto);

    void deleteEvent(Long eventId);
}
