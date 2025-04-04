package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.models.UserEntity;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
}
