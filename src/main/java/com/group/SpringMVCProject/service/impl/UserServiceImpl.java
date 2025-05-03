package com.group.SpringMVCProject.service.impl;

import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(RegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        String requestedRole = registrationDto.getRole() != null ? registrationDto.getRole() : "ROLE_USER";
        Role role = roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new RuntimeException("Role not found: " + requestedRole));

        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);

        log.info("User registered successfully: {}", registrationDto.getUsername());
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}