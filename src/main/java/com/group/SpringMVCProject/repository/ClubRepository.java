package com.group.SpringMVCProject.repository;

import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByTitle(String url);

    boolean existsByIdAndUsersContaining(Long id, UserEntity user);

//    @Modifying
//    @Query("SELECT c FROM Club c WHERE c.title LIKE CONCAT('%', :query, '%')")
//    List<Club> searchClubs(String query);
}
