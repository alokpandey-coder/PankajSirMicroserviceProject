package com.propertyservice.service;



import com.propertyservice.dto.CityDto;
import com.propertyservice.entity.City;
import com.propertyservice.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public CityDto addCity(CityDto cityDto) {
        City existing = cityRepository.findByName(cityDto.getName());
        if (existing != null) {
            throw new RuntimeException("City already exists with name: " + cityDto.getName());
        }

        City city = new City();
        city.setName(cityDto.getName());

        City saved = cityRepository.save(city);
        return new CityDto(saved.getId(), saved.getName());
    }

    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream()
                .map(c -> new CityDto(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));
        return new CityDto(city.getId(), city.getName());
    }

    public CityDto getCityByName(String name) {
        City city = cityRepository.findByName(name);
        if (city == null) {
            throw new RuntimeException("City not found with name: " + name);
        }
        return new CityDto(city.getId(), city.getName());
    }
}

