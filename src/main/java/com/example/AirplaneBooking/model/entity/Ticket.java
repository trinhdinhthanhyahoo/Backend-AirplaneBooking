package com.example.AirplaneBooking.model.entity;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ticket")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id")
    private UUID ticketId;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Column(name = "passenger_id")
    private UUID passengerId;

    @Column(name = "flight_id")
    private UUID flightId;

    @Column(name = "seat_id")
    private UUID seatId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    private String status;
}