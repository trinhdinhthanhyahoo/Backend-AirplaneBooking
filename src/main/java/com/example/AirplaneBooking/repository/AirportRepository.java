package com.example.AirplaneBooking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Airport;

@Repository
public interface AirportRepository extends JpaRepository<Airport, UUID> {
    Optional<Airport> findByAirportCode(String airportCode);

    List<Airport> findByCity(String city);

    List<Airport> findByCountry(String country);

    boolean existsByAirportCode(String airportCode);
}