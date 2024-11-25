package com.example.AirplaneBooking.dto.airplane;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AirplaneDTO {
    private UUID aircraftId;
    private UUID airlineId;
    private String aircraftCode;
    private String aircraftName;
    private String aircraftType;
    private Integer seatCapacity;
}