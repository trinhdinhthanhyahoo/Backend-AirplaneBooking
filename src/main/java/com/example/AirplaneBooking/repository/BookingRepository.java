package com.example.AirplaneBooking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Booking;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByUserId(UUID userId);

    List<Booking> findByFlight_FlightId(UUID flightId);

    List<Booking> findByStatus(String status);

    List<Booking> findByPassengerCountGreaterThan(Integer count);

    List<Booking> findByPassengerCountLessThan(Integer count);

    @Query("SELECT SUM(b.passengerCount) FROM Booking b")
    Integer countTotalPassengers();
}