package com.group.SpringMVCProject.repository;

import com.group.SpringMVCProject.models.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);
    List<PasswordReset> findByEmailAndExpireBefore(String email, LocalDateTime expire);
    Optional<PasswordReset> findByEmail(String email);
}
