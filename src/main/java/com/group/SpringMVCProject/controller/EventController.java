package com.group.SpringMVCProject.controller;

import com.group.SpringMVCProject.dto.EventDto;
import com.group.SpringMVCProject.models.Event;
import com.group.SpringMVCProject.service.EventService;
import com.group.SpringMVCProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events") // Changed to a clear REST endpoint
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend access
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping
    public List<EventDto> listEvents() {
        return eventService.findAllEvents();
    }

    @GetMapping("/{eventId}")
    public EventDto getEvent(@PathVariable Long eventId) {
        return eventService.findEventById(eventId);
    }

    @PostMapping("/new/{clubId}")
    public Event createEvent(@PathVariable Long clubId, @Valid @RequestBody EventDto eventDto) {
        return eventService.createEvent(clubId, eventDto);
    }

    @PutMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable Long eventId, @Valid @RequestBody EventDto eventDto) {
        EventDto existing = eventService.findEventById(eventId);
        eventDto.setId(eventId);
        eventDto.setClub(existing.getClub());
        return eventService.updateEvent(eventDto);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }
}
