package com.group.SpringMVCProject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    private Long id;
    @NotEmpty(message = "Username can not be empty!")
    private String username;
    @NotEmpty(message = "Email can not be empty!")
    private String email;
    @NotEmpty(message = "Password can not be empty!")
    private String password;
}
