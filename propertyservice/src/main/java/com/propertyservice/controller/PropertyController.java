package com.propertyservice.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.propertyservice.repository.RoomAvailabilityRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.PropertyDto;
import com.propertyservice.entity.RoomAvailability;
import com.propertyservice.entity.Rooms;
import com.propertyservice.service.PropertyService;


@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
	
	private PropertyService propertyService;
    private RoomAvailabilityRepository roomAvailabilityRepository;
	
	public PropertyController(PropertyService propertyService,RoomAvailabilityRepository roomAvailabilityRepository) {
		this.propertyService=propertyService;
        this.roomAvailabilityRepository=roomAvailabilityRepository;
	}
	
	@PostMapping(
		    value = "/add-property",
		    consumes = MediaType.MULTIPART_FORM_DATA_VALUE,  // Ensures the endpoint accepts multipart/form-data
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
	public ResponseEntity<APIResponse> addProperty(
	        @RequestParam("property") String propertyJson,  // Use RequestParam to get the property as a raw JSON string
	        @RequestParam("files") MultipartFile[] files) {
		// Use RequestParam to handle files
		
		// Parse the property JSON into PropertyDto
	    ObjectMapper objectMapper = new ObjectMapper();
	    PropertyDto dto = null;
	    
	    try {
	    	    dto= objectMapper.readValue(propertyJson,PropertyDto.class);
	    }catch(JsonProcessingException e){
	    	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }

	    // Process the property and files
	    PropertyDto property = propertyService.addProperty(dto, files);
	    
	    //Response
	    
	    APIResponse<PropertyDto> response = new APIResponse<>();
	    
	    response.setMessage("Property Added!!!");
	    response.setData(property);
	    response.setStatus(201);

	    return new ResponseEntity<>(response,HttpStatusCode.valueOf(response.getStatus()));
}
	
	@GetMapping("/search-property")
	public APIResponse searchProperty(
	        @RequestParam String name,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
	) {
	    APIResponse response = propertyService.searchProperty(name, date);
	    return response;
	}
	
	
	@GetMapping("/property-id")
	public APIResponse<PropertyDto> getPropertyById(@RequestParam long id){
		APIResponse<PropertyDto> response = propertyService.findPropertyById(id);
		return response;
	}
	
	@GetMapping("/room-available-room-id")
	public APIResponse<List<RoomAvailability>> getTotalRoomsAvailable(@RequestParam long id){
		List<RoomAvailability> totalRooms = propertyService.getTotalRoomsAvailable(id);
		
		APIResponse<List<RoomAvailability>> response = new APIResponse<>();
	    response.setMessage("Total rooms");
	    response.setStatus(200);
	    response.setData(totalRooms);
	    return response;
	}
	
	@GetMapping("/room-id")
	public APIResponse<Rooms> getRoomType(@RequestParam long id){
		Rooms room = propertyService.getRoomById(id);
		
		APIResponse<Rooms> response = new APIResponse<>();
	    response.setMessage("Total rooms");
	    response.setStatus(200);
	    response.setData(room);
	    return response;
	}

    //Update Room Count

    @PutMapping("/updateRoomCount")
    public APIResponse<Boolean> updateRoomCount(@RequestParam long id, @RequestParam LocalDate date){
        APIResponse<Boolean> response = new APIResponse<>();

        RoomAvailability roomsAvailable =roomAvailabilityRepository.getRooms(id,date);

        int totalAvailableRooms = roomsAvailable.getAvailableCount();
        if(totalAvailableRooms>0) {
            roomsAvailable.setAvailableCount(totalAvailableRooms - 1);
            roomAvailabilityRepository.save(roomsAvailable);
            response.setMessage("Updated");
            response.setStatus(200);
            response.setData(true);
            return response;
        }else{
            response.setMessage("Rooms are Not Available :!!!!!");
            response.setStatus(500);
            response.setData(false);
            return response;
        }
    }
}
