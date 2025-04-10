package com.group.SpringMVCProject.mapper;

import com.group.SpringMVCProject.dto.CountryDto;
import com.group.SpringMVCProject.models.Country;

public class CountryMapper {
    public static Country mapToCountry(CountryDto countryDto) {
        return Country.builder()
                .id(countryDto.getId())
                .name(countryDto.getName())
                .build();
    }

    public static CountryDto mapToClubDto(Country country) {
        return CountryDto.builder()
                .id(country.getId())
                .name(country.getName())
                .build();
    }
}
