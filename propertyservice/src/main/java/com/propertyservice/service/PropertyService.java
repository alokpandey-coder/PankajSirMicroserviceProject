package com.propertyservice.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.propertyservice.contants.AppConstants;
import com.propertyservice.dto.APIResponse;
import com.propertyservice.dto.EmailRequest;
import com.propertyservice.dto.PropertyDto;
import com.propertyservice.dto.RoomsDto;
import com.propertyservice.entity.Area;
import com.propertyservice.entity.City;
import com.propertyservice.entity.Property;
import com.propertyservice.entity.PropertyPhotos;
import com.propertyservice.entity.RoomAvailability;
import com.propertyservice.entity.Rooms;
import com.propertyservice.entity.State;
import com.propertyservice.repository.AreaRepository;
import com.propertyservice.repository.CityRepository;
import com.propertyservice.repository.PropertyPhotosRepository;
import com.propertyservice.repository.PropertyRepository;
import com.propertyservice.repository.RoomAvailabilityRepository;
import com.propertyservice.repository.RoomsRepository;
import com.propertyservice.repository.StateRepository;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private RoomsRepository roomRepository;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private PropertyPhotosRepository propertyPhotosRepository;
    
    @Autowired
    private RoomAvailabilityRepository availabilityRepository;
    
   @Autowired
   private KafkaTemplate<String, EmailRequest> kafkaTemplate;

    public PropertyDto addProperty(PropertyDto dto, MultipartFile[] files) {
        // Fetch and validate State
        State state = stateRepository.findByName(dto.getState());
        if (state == null) {
            throw new IllegalArgumentException("State not found: " + dto.getState());
        }

        // Fetch and validate City
        City city = cityRepository.findByName(dto.getCity());
        if (city == null) {
            throw new IllegalArgumentException("City not found: " + dto.getCity());
        }

        // Fetch and validate Area
        Area area = areaRepository.findByName(dto.getArea());
        if (area == null) {
            throw new IllegalArgumentException("Area not found: " + dto.getArea());
        }

        // Create Property entity
        Property property = new Property();
        property.setName(dto.getName());
        property.setNumberOfBeds(dto.getNumberOfBeds());
        property.setNumberOfRooms(dto.getNumberOfRooms());
        property.setNumberOfBathrooms(dto.getNumberOfBathrooms());
        property.setNumberOfGuestAllowed(dto.getNumberOfGuestAllowed());
        property.setState(state);
        property.setCity(city);
        property.setArea(area);

        // Save base property
        Property savedProperty = propertyRepository.save(property);

        // Add and save rooms
        if (dto.getRooms() != null) {
            for (RoomsDto roomsDto : dto.getRooms()) {
                Rooms room = new Rooms();
                room.setRoomType(roomsDto.getRoomType());
                room.setBasePrice(roomsDto.getBasePrice());
                room.setProperty(savedProperty);
                roomRepository.save(room);
            }
        }

        // Upload files and save property photos
        List<String> fileUrls = s3Service.uploadFiles(files);
        for (String url : fileUrls) {
            PropertyPhotos photo = new PropertyPhotos();
            photo.setUrl(url);
            photo.setProperty(savedProperty);
            propertyPhotosRepository.save(photo);
        }

        // Reload the property to include rooms and photos
        Property fullProperty = propertyRepository.findById(savedProperty.getId())
                .orElseThrow(() -> new RuntimeException("Property not found after save"));
        
        EmailRequest sendEmail = new EmailRequest("8577923811lion@gmail.com","Property Added","Your Property Details are now Lived");
        
        kafkaTemplate.send(AppConstants.TOPIC,sendEmail);

        return convertToDto(fullProperty);
    }
    
    
    

    private PropertyDto convertToDto(Property property) {
        PropertyDto dto = new PropertyDto();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setNumberOfBeds(property.getNumberOfBeds());
        dto.setNumberOfRooms(property.getNumberOfRooms());
        dto.setNumberOfBathrooms(property.getNumberOfBathrooms());
        dto.setNumberOfGuestAllowed(property.getNumberOfGuestAllowed());

        dto.setState(property.getState() != null ? property.getState().getName() : null);
        dto.setCity(property.getCity() != null ? property.getCity().getName() : null);
        dto.setArea(property.getArea() != null ? property.getArea().getName() : null);

        // Map rooms safely
        if (property.getRooms() != null) {
            List<RoomsDto> roomsDtoList = property.getRooms().stream()
                    .map(room -> {
                        RoomsDto roomsDto = new RoomsDto();
                        roomsDto.setRoomType(room.getRoomType());
                        roomsDto.setBasePrice(room.getBasePrice());
                        return roomsDto;
                    }).collect(Collectors.toList());
            dto.setRooms(roomsDtoList);
        }

        // Map photos safely
        if (property.getPhotos() != null) {
            List<String> imageUrls = property.getPhotos().stream()
                    .map(PropertyPhotos::getUrl)
                    .collect(Collectors.toList());
            dto.setImageUrls(imageUrls);
        }

        return dto;
    }
    
    //Search Property
    
	public APIResponse searchProperty(String name, LocalDate date) {
		
		List<Property> properties = propertyRepository.searchProperty(name,date);
		APIResponse<List<Property>> response = new APIResponse<>();
		
		response.setMessage("Search result");
		response.setStatus(200);
		response.setData(properties);
		
		return response;
	}
	
	//find PropertyBy Id
	
	public APIResponse<PropertyDto> findPropertyById(long id){
		APIResponse<PropertyDto> response = new APIResponse<>();
		PropertyDto dto  = new PropertyDto();
		Optional<Property> opProp = propertyRepository.findById(id);
		if(opProp.isPresent()) {
			Property property = opProp.get();
			dto.setArea(property.getArea().getName());
			dto.setCity(property.getCity().getName());
			dto.setState(property.getState().getName());
			List<Rooms> rooms = property.getRooms();
			List<RoomsDto> roomsDto = new ArrayList<>();
			for(Rooms room:rooms) {
				RoomsDto roomDto = new RoomsDto();
				BeanUtils.copyProperties(room, roomDto);
				roomsDto.add(roomDto);
			}
			dto.setRooms(roomsDto);
			BeanUtils.copyProperties(property, dto);
			response.setMessage("Matching Record");
			response.setStatus(200);
			response.setData(dto);
			return response;
		}
		
		return null;
	}
    
	public List<RoomAvailability> getTotalRoomsAvailable(long id) {
		return availabilityRepository.findByRoomId(id);
		
	}
	
	public Rooms getRoomById(long id) {
		return roomRepository.findById(id).get();
	}
}
