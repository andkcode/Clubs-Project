package com.group.SpringMVCProject.service;

import com.group.SpringMVCProject.dto.EventDto;

public interface EventService {

    void createEvent(Long clubId, EventDto eventDto);
}
