package com.example.AirplaneBooking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, UUID> {
    List<Seat> findByFlightId(UUID flightId);

    List<Seat> findByFlightIdAndSeatStatus(UUID flightId, String status);

    Optional<Seat> findByFlightIdAndSeatCode(UUID flightId, String seatCode);
}