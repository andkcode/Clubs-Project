package com.group.SpringMVCProject.mapper;

import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.models.Club;

public class ClubMapper {
    public static Club mapToClub(ClubDto clubDto) {
        return Club.builder()
                .id(clubDto.getId())
                .title(clubDto.getTitle())
                .photoUrl(clubDto.getPhotoUrl())
                .description(clubDto.getDescription())
                .createdBy(clubDto.getCreatedBy())
                .createdOn(clubDto.getCreatedOn())
                .updatedOn(clubDto.getUpdatedOn())
                .createdBy(clubDto.getCreatedBy())
                .build();
    }

    public static ClubDto mapToClubDto(Club club) {
        return ClubDto.builder()
                .id(club.getId())
                .title(club.getTitle())
                .photoUrl(club.getPhotoUrl())
                .description(club.getDescription())
                .createdBy(club.getCreatedBy())
                .createdOn(club.getCreatedOn())
                .updatedOn(club.getUpdatedOn())
                .createdBy(club.getCreatedBy())
                .build();
    }
}
