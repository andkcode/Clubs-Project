package com.group.SpringMVCProject.service.impl;

import com.group.SpringMVCProject.dto.RegistrationDto;
import com.group.SpringMVCProject.models.Role;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.RoleRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
