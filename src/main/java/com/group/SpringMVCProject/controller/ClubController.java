package com.group.SpringMVCProject.controller;

import com.group.SpringMVCProject.dto.ClubDto;
import com.group.SpringMVCProject.models.Club;
import com.group.SpringMVCProject.service.ClubService;
import com.group.SpringMVCProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs") // API Endpoint for Vue.js
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend to access the API (change to your frontend URL)
public class ClubController {
    private final ClubService clubService;
    private final UserService userService;

    @Autowired
    public ClubController(ClubService clubService, UserService userService) {
        this.clubService = clubService;
        this.userService = userService;
    }

    @GetMapping
    public List<ClubDto> listClubs() {
        return clubService.findAllClubs(); // Returns JSON for Vue
    }

    @GetMapping("/clubs/search")
    public List<ClubDto> searchClub(@RequestParam("query") String query) {
        return clubService.searchClubs(query); // Returns filtered results
    }

    @DeleteMapping("/{clubId}")
    public void deleteClub(@PathVariable Long clubId) {
        clubService.deleteClubById(clubId); // Deletes a club
    }

    @GetMapping("/{clubId}")
    public ClubDto clubDetail(@PathVariable Long clubId) {
        return clubService.findClubById(clubId); // Returns a single club as JSON
    }

    @PostMapping("/clubs/new")
    public Club saveClub(@Valid @RequestBody ClubDto clubDto) {
        return clubService.saveClub(clubDto); // Saves and returns the new club
    }

    @PutMapping("/{clubId}")
    public ClubDto updateClub(@PathVariable Long clubId, @Valid @RequestBody ClubDto clubDto) {
        clubDto.setId(clubId);
        return clubService.updateClub(clubDto); // Updates and returns the club
    }
}
