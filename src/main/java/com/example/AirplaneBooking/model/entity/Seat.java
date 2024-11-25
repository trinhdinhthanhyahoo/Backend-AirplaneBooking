package com.example.AirplaneBooking.model.entity;

import java.math.BigDecimal;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat")
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "seat_id")
    private UUID seatId;

    @Column(name = "seat_code")
    private String seatCode;

    @Column(name = "seat_class")
    private String seatClass;

    @Column(name = "seat_price")
    private BigDecimal seatPrice;

    @Column(name = "seat_status")
    private String seatStatus;

    @Column(name = "flight_id")
    private UUID flightId;
}