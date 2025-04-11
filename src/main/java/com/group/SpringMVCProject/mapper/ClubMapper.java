package com.group.SpringMVCProject.mapper;

import com.group.SpringMVCProject.dto.CityDto;
import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.dto.EventDto;
import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.models.Event;

import java.util.ArrayList;
import java.util.List;

public class ClubMapper {
    public static Club mapToClub(ClubDto clubDto) {
        return Club.builder()
                .id(clubDto.getId())
                .title(clubDto.getTitle())
                .category(clubDto.getCategory())
                .photoUrl(clubDto.getPhotoUrl())
                .description(clubDto.getDescription())
                .createdBy(clubDto.getCreatedBy())
                .members(clubDto.getMembers())
                .createdOn(clubDto.getCreatedOn())
                .type(clubDto.getType())
                .updatedOn(clubDto.getUpdatedOn())
                .build();
    }


    private static List<EventDto> mapEvents(List<Event> events) {
        List<EventDto> eventsDto = new ArrayList<>();
        for(Event event : events) {
            EventDto dto = EventMapper.mapToEventDto(event);
            eventsDto.add(dto);
        }

        return eventsDto;
    }

    public static ClubDto mapToClubDto(Club club) {
        return ClubDto.builder()
                .id(club.getId())
                .title(club.getTitle())
                .photoUrl(club.getPhotoUrl())
                .category(club.getCategory())
                .description(club.getDescription())
                .createdBy(club.getCreatedBy())
                .createdOn(club.getCreatedOn())
                .updatedOn(club.getUpdatedOn())
                .type(club.getType())
                .events(mapEvents(club.getEvents()))
                .members(club.getMembers())
                .cityName(club.getCity().getName())
                .countryName(club.getCity().getCountry().getName())
                .build();
    }
}
