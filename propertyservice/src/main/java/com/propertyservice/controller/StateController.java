package com.propertyservice.controller;

import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.StateDto;
import com.propertyservice.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StateController {

    @Autowired
    private StateService stateService;

    @PostMapping
    public ResponseEntity<APIResponse<StateDto>> addState(@RequestBody StateDto stateDto) {
        StateDto saved = stateService.addState(stateDto);
        APIResponse<StateDto> response = new APIResponse<>();
        response.setMessage("State added successfully");
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<StateDto>>> getAllStates() {
        List<StateDto> states = stateService.getAllStates();
        APIResponse<List<StateDto>> response = new APIResponse<>();
        response.setMessage("States fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(states);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<StateDto>> getStateById(@PathVariable Long id) {
        StateDto state = stateService.getStateById(id);
        APIResponse<StateDto> response = new APIResponse<>();
        response.setMessage("State fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(state);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<APIResponse<StateDto>> getStateByName(@PathVariable String name) {
        StateDto state = stateService.getStateByName(name);
        APIResponse<StateDto> response = new APIResponse<>();
        response.setMessage("State fetched successfully");
        response.setStatus(HttpStatus.OK.value());
        response.setData(state);
        return ResponseEntity.ok(response);
    }
}
