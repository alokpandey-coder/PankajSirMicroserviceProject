package com.propertyservice.controller;

import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.RoomsDto;
import com.propertyservice.service.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomsController {

    @Autowired
    private RoomsService roomsService;

    @PostMapping
    public ResponseEntity<APIResponse<RoomsDto>> addRoom(@RequestBody RoomsDto roomsDto) {
        RoomsDto saved = roomsService.addRoom(roomsDto);
        APIResponse<RoomsDto> response = new APIResponse<>();
        response.setMessage("Room added successfully");
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<RoomsDto>>> getAllRooms() {
        List<RoomsDto> rooms = roomsService.getAllRooms();
        APIResponse<List<RoomsDto>> response = new APIResponse<>();
        response.setMessage("Rooms fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(rooms);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<RoomsDto>> getRoomById(@PathVariable Long id) {
        RoomsDto room = roomsService.getRoomById(id);
        APIResponse<RoomsDto> response = new APIResponse<>();
        response.setMessage("Room fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(room);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<APIResponse<List<RoomsDto>>> getRoomsByProperty(@PathVariable Long propertyId) {
        List<RoomsDto> rooms = roomsService.getRoomsByPropertyId(propertyId);
        APIResponse<List<RoomsDto>> response = new APIResponse<>();
        response.setMessage("Rooms fetched successfully for property");
        response.setStatus(HttpStatus.OK.value());
        response.setData(rooms);
        return ResponseEntity.ok(response);
    }
}
