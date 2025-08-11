package com.propertyservice.service;



import com.propertyservice.dto.StateDto;
import com.propertyservice.entity.State;
import com.propertyservice.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    public StateDto addState(StateDto stateDto) {
        State existing = stateRepository.findByName(stateDto.getName());
        if (existing != null) {
            throw new RuntimeException("State already exists with name: " + stateDto.getName());
        }

        State state = new State();
        state.setName(stateDto.getName());

        State saved = stateRepository.save(state);
        return new StateDto(saved.getId(), saved.getName());
    }

    public List<StateDto> getAllStates() {
        return stateRepository.findAll().stream()
                .map(s -> new StateDto(s.getId(), s.getName()))
                .collect(Collectors.toList());
    }

    public StateDto getStateById(Long id) {
        State state = stateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("State not found with id: " + id));
        return new StateDto(state.getId(), state.getName());
    }

    public StateDto getStateByName(String name) {
        State state = stateRepository.findByName(name);
        if (state == null) {
            throw new RuntimeException("State not found with name: " + name);
        }
        return new StateDto(state.getId(), state.getName());
    }
}
