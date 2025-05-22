package com.group.SpringMVCProject.controller;


import com.group.SpringMVCProject.dto.PasswordResetDto;
import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.dto.ResetPasswordRequestDto;
import com.group.SpringMVCProject.dto.RoleDto;
import com.group.SpringMVCProject.models.PasswordReset;
import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.PasswordResetRepository;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;

import com.group.SpringMVCProject.security.models.JwtAuthenticationResponse;
import com.group.SpringMVCProject.security.services.AuthenticationService;
import com.group.SpringMVCProject.security.services.JwtService;
import com.group.SpringMVCProject.service.impl.EmailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetRepository passwordResetRepository;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final EmailServiceImpl emailService;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, JwtService jwtService, PasswordResetRepository passwordResetRepository, AuthenticationService authenticationService, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.passwordResetRepository = passwordResetRepository;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegistrationDto dto) {
        try {
            JwtAuthenticationResponse response = authenticationService.login(dto);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        RoleDto roleDto = new RoleDto(); // или создавай на стороне клиента
        roleDto.setName("ROLE_USER");

        JwtAuthenticationResponse response = authenticationService.register(dto, roleDto);
        return ResponseEntity.ok(response);
    }

    // FOR EDITING AND REPAIRING
    @PostMapping("/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegistrationDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        JwtAuthenticationResponse response = authenticationService.registerAdmin(dto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> generateResetToken(@RequestBody PasswordResetDto passwordResetDto) {
        authenticationService.generateResetToken(passwordResetDto);
        return ResponseEntity.ok("Reset link sent to your email");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        authenticationService.resetPassword(resetPasswordRequestDto);
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
