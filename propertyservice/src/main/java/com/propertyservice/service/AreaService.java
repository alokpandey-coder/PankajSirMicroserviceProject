package com.propertyservice.service;



import com.propertyservice.dto.AreaDto;
import com.propertyservice.entity.Area;
import com.propertyservice.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AreaService {

    @Autowired
    private AreaRepository areaRepository;

    public AreaDto addArea(AreaDto areaDto) {
        // Check if exists
        Area existing = areaRepository.findByName(areaDto.getName());
        if (existing != null) {
            throw new RuntimeException("Area already exists with name: " + areaDto.getName());
        }

        Area area = new Area();
        area.setName(areaDto.getName());

        Area saved = areaRepository.save(area);
        return new AreaDto(saved.getId(), saved.getName());
    }

    public List<AreaDto> getAllAreas() {
        return areaRepository.findAll().stream()
                .map(a -> new AreaDto(a.getId(), a.getName()))
                .collect(Collectors.toList());
    }

    public AreaDto getAreaById(Long id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Area not found with id: " + id));
        return new AreaDto(area.getId(), area.getName());
    }

    public AreaDto getAreaByName(String name) {
        Area area = areaRepository.findByName(name);
        if (area == null) {
            throw new RuntimeException("Area not found with name: " + name);
        }
        return new AreaDto(area.getId(), area.getName());
    }
}
