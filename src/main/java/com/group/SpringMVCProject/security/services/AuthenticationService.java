package com.group.SpringMVCProject.security.services;

import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.dto.RoleDto;
import com.group.SpringMVCProject.models.PasswordReset;
import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.PasswordResetRepository;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.security.models.JwtAuthenticationResponse;
import com.group.SpringMVCProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetRepository passwordResetRepository;

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

    public void generateResetToken(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Email not found");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expire = LocalDateTime.now().plusMinutes(15);

        PasswordReset resetToken = new PasswordReset(null, email, token, expire);
        System.out.println("Reset link: http://localhost:8080/reset-password?token=" + token);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordReset reset = passwordResetRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if(reset.getExpire().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        UserEntity user = userRepository.findByEmail(reset.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetRepository.delete(reset);
    }
}