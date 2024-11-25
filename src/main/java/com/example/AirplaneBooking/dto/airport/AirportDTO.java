package com.example.AirplaneBooking.dto.airport;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirportDTO {
    private UUID airportId;
    private String airportCode;
    private String airportName;
    private String city;
    private String country;
}