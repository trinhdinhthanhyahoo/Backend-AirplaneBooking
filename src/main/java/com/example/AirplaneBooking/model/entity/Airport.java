package com.example.AirplaneBooking.model.entity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "airport")
@Data
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "airport_id")
    private UUID airportId;

    @Column(name = "airport_name")
    private String airportName;

    @Column(name = "airport_code")
    private String airportCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;
}