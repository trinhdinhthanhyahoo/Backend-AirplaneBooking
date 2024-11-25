package com.example.AirplaneBooking.dto.passenger;

import java.util.UUID;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
    private UUID passengerId;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String citizenId;
}