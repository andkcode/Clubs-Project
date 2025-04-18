package com.group.SpringMVCProject.service.impl;


import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.ClubRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.security.SecurityUtil;
import com.group.SpringMVCProject.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.group.SpringMVCProject.mapper.ClubMapper.mapToClub;
import static com.group.SpringMVCProject.mapper.ClubMapper.mapToClubDto;

@Service
public class ClubServiceImpl implements ClubService {
    private ClubRepository clubRepository;
    private UserRepository userRepository;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ClubDto> findAllClubs() {
        List<Club> clubs = clubRepository.findAll();
        List<ClubDto> clubsLogsList = clubs.stream().map((club) -> mapToClubDto(club)).collect(Collectors.toList());
        System.out.println(clubsLogsList);
        return clubsLogsList;
    }

    @Override
    public Club saveClub(ClubDto clubDto) {
        String username = SecurityUtil.getSessionUser();
        Optional<UserEntity> user = userRepository.findByUsername(username);
        Club club = mapToClub(clubDto);
        club.setCreatedBy(user.get());
        return clubRepository.save(club);
    }

    @Override
    public ClubDto findClubById(Long clubId) {
        Club club = clubRepository.findById(clubId).get();
        return mapToClubDto(club);
    }

    @Override
    public ClubDto updateClub(ClubDto clubDto) {
        String username = SecurityUtil.getSessionUser();
        Optional<UserEntity> user = userRepository.findByUsername(username);
        Club club = mapToClub(clubDto);
        club.setCreatedBy(user.get());
        clubRepository.save(club);
        return clubDto;
    }

//    @Override
//    public

    @Override
    public void deleteClubById(Long clubId) {
        clubRepository.deleteById(clubId);
    }

    @Override
    public List<ClubDto> searchClubs(String query) {
        List<Club> clubs = clubRepository.searchClubs(query);
        return clubs.stream().map(club -> mapToClubDto(club)).collect(Collectors.toList());
    }

    @Override
    public ClubDto joinClub(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));

        UserEntity currentUser = getCurrentUser();

        if (club.getUsers() == null) {
            throw new IllegalStateException("Club users list is null");
        }

        if(!club.getUsers().contains(currentUser)) {
            club.getUsers().add(currentUser);
            Club updatedClub = clubRepository.save(club);
            return mapToClubDto(updatedClub);
        }
            return mapToClubDto(club);
    }
}
