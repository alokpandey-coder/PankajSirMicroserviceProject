package com.propertyservice.controller;


import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.CityDto;
import com.propertyservice.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping
    public ResponseEntity<APIResponse<CityDto>> addCity(@RequestBody CityDto cityDto) {
        CityDto saved = cityService.addCity(cityDto);
        APIResponse<CityDto> response = new APIResponse<>();
        response.setMessage("City added successfully");
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<CityDto>>> getAllCities() {
        List<CityDto> cities = cityService.getAllCities();
        APIResponse<List<CityDto>> response = new APIResponse<>();
        response.setMessage("Cities fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(cities);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<CityDto>> getCityById(@PathVariable Long id) {
        CityDto city = cityService.getCityById(id);
        APIResponse<CityDto> response = new APIResponse<>();
        response.setMessage("City fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(city);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<APIResponse<CityDto>> getCityByName(@PathVariable String name) {
        CityDto city = cityService.getCityByName(name);
        APIResponse<CityDto> response = new APIResponse<>();
        response.setMessage("City fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(city);
        return ResponseEntity.ok(response);
    }
}
