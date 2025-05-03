package com.group.SpringMVCProject.controller;

import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/{username}/admin")
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

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() ->
                new RuntimeException("ROLE_ADMIN not found"));

        user.getRoles().add(adminRole);
        userRepository.save(user);

        return ResponseEntity.ok("User promoted to admin");
    }
}