package com.example.AirplaneBooking.dto.airline;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirlineDTO {
    private UUID airlineId;
    private String airlineName;
    private String airlineCode;
    private String hotline;
}