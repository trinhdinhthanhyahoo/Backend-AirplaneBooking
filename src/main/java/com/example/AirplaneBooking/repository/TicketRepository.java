package com.example.AirplaneBooking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByBookingId(UUID bookingId);

    List<Ticket> findByPassengerId(UUID passengerId);

    List<Ticket> findByFlightId(UUID flightId);

    List<Ticket> findByStatus(String status);

}