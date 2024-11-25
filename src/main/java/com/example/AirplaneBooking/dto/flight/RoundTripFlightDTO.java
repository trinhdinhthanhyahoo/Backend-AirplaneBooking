package com.example.AirplaneBooking.dto.flight;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundTripFlightDTO {
    private List<FlightDTO> departureFlights;
    private List<FlightDTO> returnFlights;
}