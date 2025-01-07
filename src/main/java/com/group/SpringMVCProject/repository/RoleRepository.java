package com.group.SpringMVCProject.repository;

import com.group.SpringMVCProject.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
