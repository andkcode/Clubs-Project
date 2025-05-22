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

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        Role defaultRole = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new RuntimeException("ROLE_USER not found"));
        ;
        user.setRoles(Collections.singletonList(defaultRole));

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken));
    }

    @PostMapping("/admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegistrationDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role defaultRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() ->
                new RuntimeException("ROLE_ADMIN not found"));
        ;
        user.setRoles(Collections.singletonList(defaultRole));

        userRepository.save(user);
        return ResponseEntity.ok("Admin registered");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetDto passwordResetDto) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(passwordResetDto.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
        }

        String token = UUID.randomUUID().toString();
        PasswordReset reset = new PasswordReset(null, passwordResetDto.getEmail(), token, LocalDateTime.now().plusMinutes(15));
        passwordResetRepository.save(reset);

        emailService.sendResetEmail(passwordResetDto.getEmail(), token);

        return ResponseEntity.ok("Reset link sent to your email");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,
                                                @RequestParam("newPassword") String newPassword) {
        authenticationService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}
