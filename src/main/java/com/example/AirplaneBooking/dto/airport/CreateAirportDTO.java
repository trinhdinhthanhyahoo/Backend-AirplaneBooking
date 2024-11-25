package com.example.AirplaneBooking.dto.airport;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAirportDTO {
    private String airportCode;
    private String airportName;
    private String city;
    private String country;
}