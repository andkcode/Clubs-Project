package com.group.SpringMVCProject.security.services;

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
import com.group.SpringMVCProject.service.UserService;
import com.group.SpringMVCProject.service.impl.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetRepository passwordResetRepository;

    @Transactional
    public JwtAuthenticationResponse register(RegistrationDto registrationDto, RoleDto roleDto) {
        userService.saveUser(registrationDto, roleDto);
        try {
            userService.saveUser(registrationDto, roleDto);

        UserEntity user = userRepository.findByEmail(registrationDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created user"));
            UserEntity user = userRepository.findByEmail(registrationDto.getEmail())
                    .orElseThrow(() -> new RuntimeException("Failed to retrieve created user"));

            String jwt = jwtService.generateToken(user);

            return JwtAuthenticationResponse.builder()
                    .token(jwt)
                    .build();
        } catch (DataAccessException e) {
            log.error("Database error during user registration", e);
            throw new RuntimeException("Registration failed due to database error");
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            throw new RuntimeException("Registration failed");
        }
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
            log.warn("Bad credentials for email: {}", registrationDto.getEmail());
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    @Transactional
    public JwtAuthenticationResponse registerAdmin(RegistrationDto dto) {
        try {
            RoleDto roleDto = new RoleDto();
            roleDto.setName("ROLE_ADMIN");
            return register(dto, roleDto);
        } catch (Exception e) {
            log.error("Failed to register admin user", e);
            throw new RuntimeException("Admin registration failed");
        }
    }

    @Transactional
    public void generateResetToken(PasswordResetDto passwordResetDto) {
        try {
            // Validate email
            if (passwordResetDto.getEmail() == null || passwordResetDto.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }

            UserEntity user = userRepository.findByEmail(passwordResetDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Clean up any existing expired tokens for this email
            cleanupExpiredTokens(passwordResetDto.getEmail());

            String token = UUID.randomUUID().toString();
            log.debug("Generated reset token for email: {}", passwordResetDto.getEmail());

            LocalDateTime expire = LocalDateTime.now().plusMinutes(15);

            PasswordReset resetToken = new PasswordReset(null, passwordResetDto.getEmail(), token, expire);
            PasswordReset saved = passwordResetRepository.save(resetToken);
            log.debug("Saved reset token to database for email: {}", passwordResetDto.getEmail());

            // Send email after successful database save
            emailService.sendResetEmail(passwordResetDto.getEmail(), token);

        } catch (UsernameNotFoundException e) {
            log.warn("Password reset attempted for non-existent email: {}", passwordResetDto.getEmail());
            // Don't throw exception to prevent email enumeration
            // Instead, log and silently ignore
        } catch (Exception e) {
            log.error("Error generating reset token for email: {}", passwordResetDto.getEmail(), e);
            throw new RuntimeException("Failed to generate reset token");
        }
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