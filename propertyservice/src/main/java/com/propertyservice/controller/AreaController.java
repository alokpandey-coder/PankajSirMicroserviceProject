package com.propertyservice.controller;

import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.AreaDto;
import com.propertyservice.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @PostMapping
    public ResponseEntity<APIResponse<AreaDto>> addArea(@RequestBody AreaDto areaDto) {
        AreaDto saved = areaService.addArea(areaDto);
        APIResponse<AreaDto> response = new APIResponse<>();
        response.setMessage("Area added successfully");
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<AreaDto>>> getAllAreas() {
        List<AreaDto> areas = areaService.getAllAreas();
        APIResponse<List<AreaDto>> response = new APIResponse<>();
        response.setMessage("Areas fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(areas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<AreaDto>> getAreaById(@PathVariable Long id) {
        AreaDto area = areaService.getAreaById(id);
        APIResponse<AreaDto> response = new APIResponse<>();
        response.setMessage("Area fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(area);
        return ResponseEntity.ok(response);
    }
}

