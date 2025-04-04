package com.group.SpringMVCProject.controller;

import com.group.SpringMVCProject.dto.EventDto;
import com.group.SpringMVCProject.models.Event;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.security.SecurityUtil;
import com.group.SpringMVCProject.service.EventService;
import com.group.SpringMVCProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class EventController {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, UserService userService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public String eventList(Model model) {
        Optional<UserEntity> user = Optional.of(new UserEntity());
        List<EventDto> events = eventService.findAllEvents();
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = Optional.ofNullable(userService.findByUsername(username));
            model.addAttribute("user",user);
        }
        model.addAttribute("user",user);
        model.addAttribute("events", events);
        return "events-list";
    }

    @GetMapping("/events/{eventId}")
    public String viewEvent(@PathVariable("eventId") Long eventId, Model model) {
        Optional<UserEntity> user = Optional.of(new UserEntity());
        EventDto eventDto = eventService.findEventById(eventId);
        String username = SecurityUtil.getSessionUser();
        if(username != null) {
            user = Optional.ofNullable(userService.findByUsername(username));
            model.addAttribute("user",user);
        }
        model.addAttribute("club",eventDto);
        model.addAttribute("user",user);
        model.addAttribute("event", eventDto);
        return "events-detail";
    }

    @GetMapping("/events/{clubId}/new")
    public String createEventForm(@PathVariable("clubId") Long clubId, Model model) {
        Event event = new Event();
        model.addAttribute("clubId", clubId);
        model.addAttribute("event", event);
        return "events-create";
    }

    @PostMapping("/events/{clubId}")
    public String createEvent(@PathVariable("clubId") Long clubId,@Valid @ModelAttribute("event") EventDto eventDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("event",eventDto);
            return "events-create";
        }
        eventService.createEvent(clubId, eventDto);
        return "redirect:/clubs/" + clubId;
    }

    @GetMapping("/events/{eventId}/edit")
    public String editEventForm(@PathVariable("eventId") Long eventId, Model model) {
        EventDto event = eventService.findEventById(eventId);
        model.addAttribute("event",event);
        return "events-edit";
    }

    @PostMapping("/events/{eventId}/edit")
    public String updateEvent(@PathVariable("eventId") Long eventId, @Valid @ModelAttribute("event") EventDto event, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("event",event);
            return "events-edit";
        }
        EventDto eventDto = eventService.findEventById(eventId);
        event.setId(event.getId());
        event.setClub(eventDto.getClub());
        eventService.updateEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/events/{eventId}/delete")
    public String deleteEvent(@PathVariable("eventId") Long eventId) {
        eventService.deleteEvent(eventId);
        return "redirect:/events";
    }
}
