package com.example.AirplaneBooking.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Passenger;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
    Optional<Passenger> findByCitizenId(String citizenId);

    List<Passenger> findByFullNameContainingIgnoreCase(String fullName);
}