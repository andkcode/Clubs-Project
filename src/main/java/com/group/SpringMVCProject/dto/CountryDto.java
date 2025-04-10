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
public class CountryDto {
    private Long id;
    @NotEmpty(message = "Country name should not be empty")
    private String name;
}