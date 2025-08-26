package com.propertyservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.propertyservice.entity.RoomAvailability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomAvailabilityRepository extends JpaRepository<RoomAvailability, Long> {

	public List<RoomAvailability> findByRoomId(long id);

    @Query("Select r from RoomAvailability ra where ra.roomId= :roomId AND ra.availableDate= :date")
    public RoomAvailability getRooms(@Param("roomId") long roomId, @Param("date") LocalDate date);

}
