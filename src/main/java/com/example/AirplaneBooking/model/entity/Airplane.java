package com.example.AirplaneBooking.model.entity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airplane")
@Data
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "aircraft_id")
    private UUID aircraftId;

    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Column(name = "aircraft_name")
    private String aircraftName;

    @Column(name = "aircraft_type")
    private String aircraftType;

    @Column(name = "seat_capacity")
    private Integer seatCapacity;

    @Column(name = "airline_id")
    private UUID airlineId;

    public Integer getTotalSeats() {
        return seatCapacity;
    }
}