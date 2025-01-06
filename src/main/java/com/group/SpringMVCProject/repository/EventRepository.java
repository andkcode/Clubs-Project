package com.group.SpringMVCProject.repository;

import com.group.SpringMVCProject.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository  extends JpaRepository<Event, Long> {

}
