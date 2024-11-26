package com.example.AirplaneBooking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Flight;
import com.example.AirplaneBooking.model.enums.FlightStatus;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {
        // Tìm chuyến bay theo mã
        Optional<Flight> findByFlightCode(String flightCode);

        // Kiểm tra mã chuyến bay đã tồn tại chưa
        boolean existsByFlightCode(String flightCode);

        @Query("SELECT f FROM Flight f " +
                        "WHERE f.departureAirport.airportId = :departureAirportId " +
                        "AND f.arrivalAirport.airportId = :arrivalAirportId " +
                        "AND f.departureDateTime >= :startDateTime " +
                        "AND f.departureDateTime < :endDateTime")
        List<Flight> findByAirportsAndDateRange(
                        @Param("departureAirportId") UUID departureAirportId,
                        @Param("arrivalAirportId") UUID arrivalAirportId,
                        @Param("startDateTime") LocalDateTime startDateTime,
                        @Param("endDateTime") LocalDateTime endDateTime);

        @Query("SELECT COUNT(b) FROM Booking b JOIN b.flight f WHERE f.flightId = :flightId")
        Integer countBookedSeatsByFlightId(@Param("flightId") UUID flightId);

        List<Flight> findByStatus(FlightStatus status);

        @Query("SELECT f FROM Flight f " +
                        "WHERE f.departureAirport.airportId = :departureAirportId " +
                        "AND f.arrivalAirport.airportId = :arrivalAirportId " +
                        "AND f.departureDateTime >= :startDateTime " +
                        "AND f.departureDateTime < :endDateTime " +
                        "AND f.status != 'CANCELLED'")
        List<Flight> findByAirportsAndDateRangeAndStatusNotCancelled(
                        @Param("departureAirportId") UUID departureAirportId,
                        @Param("arrivalAirportId") UUID arrivalAirportId,
                        @Param("startDateTime") LocalDateTime startDateTime,
                        @Param("endDateTime") LocalDateTime endDateTime);
}