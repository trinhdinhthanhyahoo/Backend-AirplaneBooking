package com.example.AirplaneBooking.dto.seat;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSeatDTO {
    private String seatCode;
    private String seatClass;
    private BigDecimal seatPrice;
    private String seatStatus;
    private UUID flightId;
}