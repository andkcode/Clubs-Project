package com.group.SpringMVCProject.repository;

import com.group.SpringMVCProject.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository  extends JpaRepository<Event, Long> {

    List<Event> findByClubId(Long clubId);
}
