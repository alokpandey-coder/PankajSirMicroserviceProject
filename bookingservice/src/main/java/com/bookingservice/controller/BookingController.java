package com.bookingservice.controller;

import com.bookingservice.client.PropertyClient;
import com.bookingservice.dto.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    private PropertyClient propertyClient;

    @PostMapping("/add-to-cart")
    public APIResponse<List<String>> cart(@RequestBody BookingDto bookingDto){

        APIResponse<List<String>> apiResponse = new APIResponse<>();

        List<String> message = new ArrayList<>();

        //Here we get Property on the basis of PropertyId which we get during Booking
        APIResponse<PropertyDto> response = propertyClient.getPropertyById(bookingDto.getPropertyId());

        //Here we get Total no. of Rooms on the basis of RoomAvailabilityId which we get during Booking
        APIResponse<List<RoomAvailability>> totalRoomsAvailable = propertyClient.getTotalRoomsAvailable(bookingDto.getRoomAvailabilityId());

        //Here we get Room Type on the basis of RoomId which we get during Booking
        APIResponse<Rooms> roomType = propertyClient.getRoomType(bookingDto.getRoomId());

        //List of Available Rooms
        List<RoomAvailability> availableRooms = totalRoomsAvailable.getData();

        // Now check room is available on that specific date and also check total rooms are also available on that day





      return null;
    }
}
