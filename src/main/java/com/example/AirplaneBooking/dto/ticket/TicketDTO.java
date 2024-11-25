package com.example.AirplaneBooking.dto.ticket;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private UUID ticketId;
    private UUID bookingId;
    private UUID passengerId;
    private UUID seatId;
    private BigDecimal price;
    private String status;
}