package com.group.SpringMVCProject.repository;

import com.group.SpringMVCProject.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
