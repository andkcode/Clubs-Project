package com.group.SpringMVCProject.controller;


import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        Role defaultRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(defaultRole));

        userRepository.save(user);
        return ResponseEntity.ok("User registered");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Logged in successfully");
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

        Role defaultRole = roleRepository.findByName("ROLE_ADMIN");
        user.setRoles(Collections.singletonList(defaultRole));

        userRepository.save(user);
        return ResponseEntity.ok("Admin registered");
    }

    @PutMapping("/users/{username}/admin")
    public ResponseEntity<String> promoteToAdmin(@PathVariable String username) {
        UserEntity user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        boolean isAlreadyAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (isAlreadyAdmin) {
            return ResponseEntity.badRequest().body("User is already an admin");
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");

        if (adminRole == null) {
            return ResponseEntity.internalServerError().body("Admin role not found in DB");
        }

        user.getRoles().add(adminRole);
        userRepository.save(user);

        return ResponseEntity.ok("User promoted to admin");
    }
}

