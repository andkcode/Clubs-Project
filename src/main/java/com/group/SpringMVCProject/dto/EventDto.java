package com.group.SpringMVCProject.dto;

import com.group.SpringMVCProject.models.Club;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;
    @NotEmpty(message = "Event name should not be empty")
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull(message = "Start Time should not be empty")
    private LocalDateTime startTime;
    @NotNull(message = "End Time should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    @NotEmpty(message = "Event type should not be empty")
    private String type;
    @NotEmpty(message = "Photo URL should not be empty")
    private String photoUrl;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Club club;
}
