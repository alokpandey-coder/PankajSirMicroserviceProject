package com.bookingservice.repository;



import com.bookingservice.entity.BookingDate;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bookingservice.entity.Bookings;

public interface BookingDateRepository extends JpaRepository<BookingDate, Long> {

}