package com.group.SpringMVCProject.mapper;

import com.group.SpringMVCProject.dto.EventDto;
import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.models.Event;

public class EventMapper {
    public static Event mapToEvent(EventDto eventDto) {
        return Event.builder()
                .id(eventDto.getId())
                .name(eventDto.getName())
                .startTime(eventDto.getStartTime())
                .endTime(eventDto.getEndTime())
                .type(eventDto.getType())
                .photoUrl(eventDto.getPhotoUrl())
                .createdOn(eventDto.getCreatedOn())
                .updatedOn(eventDto.getUpdatedOn())
                .club(new Club(eventDto.getClubId()))
                .build();
    }

    public static EventDto mapToEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .type(event.getType())
                .photoUrl(event.getPhotoUrl())
                .createdOn(event.getCreatedOn())
                .updatedOn(event.getUpdatedOn())
                // Здесь можно оставить только ID клуба
                .clubId(event.getClub().getId())
                .build();
    }

    // Обновлённый метод, который не содержит Club в EventDto
    public static EventDto mapToEventDtoWithoutClub(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .type(event.getType())
                .photoUrl(event.getPhotoUrl())
                .createdOn(event.getCreatedOn())
                .updatedOn(event.getUpdatedOn())
                // Только ID клуба
                .clubId(event.getClub().getId())
                .build();
    }
}
