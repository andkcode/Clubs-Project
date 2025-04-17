package com.group.SpringMVCProject.service.impl;


import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.models.UserEntity;
import com.group.SpringMVCProject.repository.ClubRepository;
import com.group.SpringMVCProject.repository.UserRepository;
import com.group.SpringMVCProject.security.SecurityUtil;
import com.group.SpringMVCProject.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.group.SpringMVCProject.mapper.ClubMapper.mapToClub;
import static com.group.SpringMVCProject.mapper.ClubMapper.mapToClubDto;

@Service
public class ClubServiceImpl implements ClubService {
    private ClubRepository clubRepository;
    private UserRepository userRepository;

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
}
