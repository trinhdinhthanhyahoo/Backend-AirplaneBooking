package com.example.AirplaneBooking.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.AirplaneBooking.model.enums.FlightStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flight")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @Column(name = "flight_id")
    private UUID flightId;

    @Column(name = "flight_code")
    private String flightCode;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private Airplane aircraft;

    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private Airport departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private Airport arrivalAirport;

    @Column(name = "departure_datetime")
    private LocalDateTime departureDateTime;

    @Column(name = "arrival_datetime")
    private LocalDateTime arrivalDateTime;

    @Column(name = "base_fare")
    private BigDecimal baseFare;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status = FlightStatus.SCHEDULED;

    public void updateAvailableSeats(Integer bookedSeats) {
        if (this.aircraft != null && this.aircraft.getTotalSeats() != null) {
            this.availableSeats = this.aircraft.getTotalSeats() - bookedSeats;
        } else {
            throw new IllegalStateException("Aircraft or total seats information is missing");
        }
    }

    public FlightStatus getStatus() {
        return status;
    }
}