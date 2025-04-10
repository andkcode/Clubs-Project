package com.group.SpringMVCProject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CityDto {
    private Long id;
    @NotEmpty(message = "City name should not be empty")
    private String name;
}