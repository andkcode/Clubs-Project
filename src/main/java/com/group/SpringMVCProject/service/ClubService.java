package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.models.Club;

import java.util.List;

public interface ClubService {
    List<ClubDto> findAllClubs();
    Club saveClub(ClubDto clubDto);

    ClubDto findClubById(Long clubId);

    ClubDto updateClub(ClubDto club);

    void deleteClubById(Long clubId);
    List<ClubDto> searchClubs(String query);
}
