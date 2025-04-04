package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.EventDto;

import java.util.List;

public interface EventService {

    void createEvent(Long id, EventDto eventDto);

    List<EventDto> findAllEvents();

    EventDto findEventById(Long id);

    void updateEvent(EventDto eventDto);

    void deleteEvent(Long id);
}
