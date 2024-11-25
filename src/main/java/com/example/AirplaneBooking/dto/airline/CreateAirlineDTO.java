package com.example.AirplaneBooking.dto.airline;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAirlineDTO {
    private String airlineName;
    private String airlineCode;
    private String hotline;
}