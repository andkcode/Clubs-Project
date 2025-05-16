package com.group.SpringMVCProject.security.services;

import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.dto.RoleDto;
import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.security.models.JwtAuthenticationResponse;
import com.group.SpringMVCProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse register(RegistrationDto registrationDto, RoleDto roleDto) {
        userService.saveUser(registrationDto, roleDto);

        UserEntity user = userRepository.findByEmail(registrationDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created user"));

        String jwt = jwtService.generateToken(user);

        return JwtAuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public JwtAuthenticationResponse login(RegistrationDto registrationDto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            registrationDto.getEmail(),
                            registrationDto.getPassword()
                    )
            );

            UserEntity user = userRepository.findByEmail(registrationDto.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

            String jwt = jwtService.generateToken(user);
            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .build();
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}