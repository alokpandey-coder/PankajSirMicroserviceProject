package com.bookingservice.controller;

import com.bookingservice.client.PropertyClient;
import com.bookingservice.dto.*;
import com.bookingservice.entity.BookingDate;
import com.bookingservice.entity.Bookings;
import com.bookingservice.repository.BookingDateRepository;
import com.bookingservice.repository.BookingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/booking")
public class BookingController {

    @Autowired
    private PropertyClient propertyClient;

    @Autowired
    private BookingsRepository bookingRepository;

    @Autowired
    private BookingDateRepository bookingDateRepository;

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

        for(LocalDate date: bookingDto.getDate()) {

            boolean isAvailable = availableRooms.stream()
                    .anyMatch(ra -> ra.getAvailableDate().equals(date) && ra.getAvailableCount() > 0);

            System.out.println("Date " + date + "available: " + isAvailable);

            // If Room is not Available
            if (!isAvailable) {

                message.add("Room is not available on : " + date);
                apiResponse.setMessage("Rooms not Available");
                apiResponse.setStatus(500);
                apiResponse.setData(message);
                return apiResponse;
            }
            
        }
            // If Room is Available : Save it to Booking Table with status pending

            Bookings bookings = new Bookings();
            bookings.setName(bookingDto.getName());
            bookings.setEmail(bookingDto.getEmail());
            bookings.setMobile(bookingDto.getMobile());
            bookings.setPropertyName(response.getData().getName());
            bookings.setStatus("pending");
            bookings.setTotalPrice(roomType.getData().getBasePrice()*bookingDto.getTotalNights());


            //Save this Booking

            Bookings savedBooking = bookingRepository.save(bookings);

            //Define Booking done on which Date + Update Room Count

        for(LocalDate date : bookingDto.getDate()){

            BookingDate bookingDate = new BookingDate();
            bookingDate.setDate(date);
            bookingDate.setBookings(bookings);

            BookingDate savedBookingDate = bookingDateRepository.save(bookingDate);

            // call to update room count using feign client :

            if(savedBookingDate !=null){

                Optional<RoomAvailability> matchedRoom = availableRooms.stream()
                        .filter(ra -> ra.getAvailableDate().equals(date) && ra.getAvailableCount() > 0)
                        .findFirst();

                matchedRoom.ifPresent(room ->
                        propertyClient.updateRoomCount(room.getId(), LocalDate.parse(date.toString())));

            }

        }
      return null;
    }
}

// video- 28:00 for URL and ----41:06