package com.example.AirplaneBooking.dto.flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.example.AirplaneBooking.model.enums.FlightStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTO {
    private UUID flightId;
    private String flightCode;
    private UUID aircraftId;
    private UUID departureAirportId;
    private UUID arrivalAirportId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalDateTime;

    private BigDecimal baseFare;
    private Integer availableSeats;
    private FlightStatus status;

    void setStatus(FlightStatus status) {
        this.status = status;
    }
}