package com.group.SpringMVCProject.security.config;

import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");

        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() ->
                    new RuntimeException("ROLE_ADMIN not found"));

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRoles(Collections.singletonList(adminRole));

            userRepository.save(admin);
            log.info("Created admin user: {}", admin.getEmail());
        }
    }

    private void createRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
            log.info("Created role: {}", roleName);
        }
    }
}

