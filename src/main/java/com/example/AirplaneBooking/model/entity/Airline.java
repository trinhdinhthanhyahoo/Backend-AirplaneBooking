package com.example.AirplaneBooking.model.entity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airline")
@Data
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "airline_id")
    private UUID airlineId;

    @Column(name = "airline_name")
    private String airlineName;

    @Column(name = "airline_code")
    private String airlineCode;

    @Column(name = "hotline")
    private String hotline;
}