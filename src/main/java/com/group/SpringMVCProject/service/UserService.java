package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.models.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {
    void saveUser(RegistrationDto registrationDto);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
}
