package com.example.AirplaneBooking.dto.flight;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchDTO {
    private UUID departureAirportId;
    private UUID arrivalAirportId;
    private LocalDate departureDate;
    private Boolean isRoundTrip;
    private LocalDate returnDate;
}