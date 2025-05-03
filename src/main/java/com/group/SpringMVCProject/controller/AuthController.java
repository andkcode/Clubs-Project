package com.group.SpringMVCProject.controller;


import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;

import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RegistrationDto dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail()).orElse(null);
        if(user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }
        return ResponseEntity.ok("Login Successful");
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


        Role defaultRole = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new RuntimeException("ROLE_USER not found"));;
        user.setRoles(Collections.singletonList(defaultRole));

        userRepository.save(user);
        return ResponseEntity.ok("User registered");
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
                new RuntimeException("ROLE_ADMIN not found"));;
        user.setRoles(Collections.singletonList(defaultRole));

        userRepository.save(user);
        return ResponseEntity.ok("Admin registered");
    }

}

