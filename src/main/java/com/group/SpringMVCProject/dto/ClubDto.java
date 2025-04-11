package com.group.SpringMVCProject.dto;

import com.group.SpringMVCProject.models.City;
import com.group.SpringMVCProject.models.Event;
import com.group.SpringMVCProject.models.UserEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubDto {
    private Long id;
    @NotEmpty(message = "Club title should not be empty")
    private String title;
    @NotEmpty(message = "Photo link should not be empty")
    private String photoUrl;
    @NotEmpty(message = "Description should not be empty")
    private String description;
    private String category;
    private UserEntity createdBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String location;
    private String cityName;
    private String countryName;
    private List<EventDto> events;
    private Integer members;
}
