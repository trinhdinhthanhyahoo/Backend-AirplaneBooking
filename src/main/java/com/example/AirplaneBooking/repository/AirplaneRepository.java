package com.example.AirplaneBooking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Airplane;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, UUID> {
    List<Airplane> findByAirlineId(UUID airlineId);

    Optional<Airplane> findByAircraftCode(String aircraftCode);

    boolean existsByAircraftCode(String aircraftCode);
}