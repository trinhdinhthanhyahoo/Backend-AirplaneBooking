package com.example.AirplaneBooking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Booking;
import com.example.AirplaneBooking.model.entity.Flight;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT b FROM Booking b JOIN FETCH b.flight WHERE b.bookingId = :id")
    Optional<Booking> findByIdWithFlight(@Param("id") UUID id);

    void deleteByFlight_FlightId(UUID flightId);

    @Query("SELECT b FROM Booking b JOIN FETCH b.flight WHERE b.bookingReference = :reference")
    Optional<Booking> findByBookingReferenceWithFlight(@Param("reference") String reference);
}