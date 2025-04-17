package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.models.UserEntity;

import java.util.Optional;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsername(String username);
}
