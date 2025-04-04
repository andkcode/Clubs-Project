package com.group.SpringMVCProject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    @NotEmpty(message = "Event name should not be empty")
    private String name;
    @NotEmpty(message = "Start Time should not be empty")
    private LocalDateTime startTime;
    @NotEmpty(message = "End Time should not be empty")
    private LocalDateTime endTime;
    @NotEmpty(message = "Event type should not be empty")
    private String type;
    @NotEmpty(message = "Photo URL should not be empty")
    private String photoUrl;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Long clubId;
}
