package com.group.SpringMVCProject.mapper;

import com.group.SpringMVCProject.dto.CityDto;
import com.group.SpringMVCProject.dto.CountryDto;
import com.group.SpringMVCProject.models.City;
import com.group.SpringMVCProject.models.Country;

public class CityMapper {
    public static City mapToCity(CityDto cityDto) {
        return City.builder()
                .id(cityDto.getId())
                .name(cityDto.getName())
                .build();
    }

    public static CityDto mapToCityDto(City city) {
        return CityDto.builder()
                .id(city.getId())
                .name(city.getName())
                .build();
    }
}
