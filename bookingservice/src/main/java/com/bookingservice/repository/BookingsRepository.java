package com.bookingservice.repository;

import com.bookingservice.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingsRepository extends JpaRepository<Bookings,Long> {
}
