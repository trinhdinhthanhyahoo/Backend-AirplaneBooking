package com.example.AirplaneBooking.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Airline;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, UUID> {
    Optional<Airline> findByAirlineCode(String airlineCode);

    boolean existsByAirlineCode(String airlineCode);
}