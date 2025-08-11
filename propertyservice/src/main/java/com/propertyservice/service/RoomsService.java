package com.propertyservice.service;



import com.propertyservice.dto.RoomsDto;
import com.propertyservice.entity.Property;
import com.propertyservice.entity.Rooms;
import com.propertyservice.repository.PropertyRepository;
import com.propertyservice.repository.RoomsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomsService {

    @Autowired
    private RoomsRepository roomsRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public RoomsDto addRoom(RoomsDto roomsDto) {
        Property property = propertyRepository.findById(roomsDto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + roomsDto.getPropertyId()));

        Rooms room = new Rooms();
        room.setRoomType(roomsDto.getRoomType());
        room.setBasePrice(roomsDto.getBasePrice());
        room.setProperty(property);

        Rooms saved = roomsRepository.save(room);
        return new RoomsDto(saved.getId(), saved.getRoomType(), saved.getBasePrice(), saved.getProperty().getId());
    }

    public List<RoomsDto> getRoomsByPropertyId(Long propertyId) {
        List<Rooms> rooms = roomsRepository.findByPropertyId(propertyId);
        return rooms.stream()
                .map(r -> new RoomsDto(r.getId(), r.getRoomType(), r.getBasePrice(), r.getProperty().getId()))
                .collect(Collectors.toList());
    }

    public RoomsDto getRoomById(Long id) {
        Rooms room = roomsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return new RoomsDto(room.getId(), room.getRoomType(), room.getBasePrice(), room.getProperty().getId());
    }

    public List<RoomsDto> getAllRooms() {
        return roomsRepository.findAll().stream()
                .map(r -> new RoomsDto(r.getId(), r.getRoomType(), r.getBasePrice(), r.getProperty().getId()))
                .collect(Collectors.toList());
    }
}
