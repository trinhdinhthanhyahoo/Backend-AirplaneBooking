package com.example.AirplaneBooking.dto.flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFlightDTO {
    private String flightCode;
    private UUID aircraftId;
    private UUID departureAirportId;
    private UUID arrivalAirportId;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private BigDecimal baseFare;
    private Integer availableSeats;
}