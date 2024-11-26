package com.example.AirplaneBooking.dto.booking;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private UUID bookingId;
    private UUID userId;
    private UUID flightId;
    private String bookingReference;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private LocalDateTime bookingDate;
    private Integer passengerCount;
    private List<String> seatCodes;
}