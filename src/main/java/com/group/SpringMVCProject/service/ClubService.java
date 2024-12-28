package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.models.Club;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClubService {
    List<ClubDto> findAllClubs();
    Club saveClub(ClubDto clubDto);

    ClubDto findClubById(long clubId);

    void updateClub(ClubDto club);

    void deleteClubById(Long clubId);
}
