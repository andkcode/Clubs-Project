package com.group.SpringMVCProject.service.impl;

import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.ClubRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.service.ClubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.group.SpringMVCProject.mapper.ClubMapper.mapToClub;
import static com.group.SpringMVCProject.mapper.ClubMapper.mapToClubDto;

@Service
@Slf4j
@Transactional
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }

        String email = authentication.getName();

        Optional<UserEntity> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()) {
            return userByEmail.get();
        }

        return userRepository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    @Override
    public boolean isUserMemberOfClub(Long clubId) {
        UserEntity currentUser = getCurrentUser();
        return clubRepository.existsByIdAndUsersContaining(clubId,currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubDto> findAllClubs() {
        List<Club> clubs = clubRepository.findAll();
        return clubs.stream()
                .map(club -> mapToClubDto(club))
                .collect(Collectors.toList());
    }

    @Override
    public Club saveClub(ClubDto clubDto) {
        UserEntity currentUser = getCurrentUser();
        Club club = mapToClub(clubDto);
        club.setCreatedBy(currentUser);

        if (club.getUsers() == null) {
            club.setUsers(new ArrayList<>());
        }

        if (!club.getUsers().contains(currentUser)) {
            club.getUsers().add(currentUser);
        }

        log.info("Creating new club: {}", club.getTitle());
        return clubRepository.save(club);
    }

    @Override
    @Transactional(readOnly = true)
    public ClubDto findClubById(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("Club not found with ID: " + clubId));
        return mapToClubDto(club);
    }

    @Override
    public ClubDto updateClub(ClubDto clubDto) {
        Club existingClub = clubRepository.findById(clubDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Club not found with ID: " + clubDto.getId()));

        UserEntity currentUser = getCurrentUser();

        if (!existingClub.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the club creator can update the club");
        }

        Club club = mapToClub(clubDto);
        club.setCreatedBy(currentUser);

        club.setUsers(existingClub.getUsers());

        log.info("Updating club: {}", club.getId());
        clubRepository.save(club);
        return mapToClubDto(club);
    }

    @Override
    public void deleteClubById(Long clubId) {
        Club existingClub = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("Club not found with ID: " + clubId));

        UserEntity currentUser = getCurrentUser();

        if (!existingClub.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Only the club creator can delete the club");
        }

        log.info("Deleting club: {}", clubId);
        clubRepository.deleteById(clubId);
    }

    @Override
    @Transactional
    public List<ClubDto> searchClubs(String query) {
        List<Club> clubs = clubRepository.searchClubs(query);
        return clubs.stream()
                .map(club -> mapToClubDto(club))
                .collect(Collectors.toList());
    }

    @Override
    public ClubDto joinClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("Club not found with ID: " + clubId));

        UserEntity currentUser = getCurrentUser();

        if (club.getUsers() == null) {
            club.setUsers(new ArrayList<>());
        }

        if (!club.getUsers().contains(currentUser)) {
            club.getUsers().add(currentUser);
            log.info("User {} joined club: {}", currentUser.getUsername(), club.getTitle());
            Club updatedClub = clubRepository.save(club);
            return mapToClubDto(updatedClub);
        }

        log.info("User {} is already a member of club: {}", currentUser.getUsername(), club.getTitle());
        return mapToClubDto(club);
    }
}