package com.example.AirplaneBooking.dto.payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private UUID paymentId;
    private UUID bookingId;
    private UUID paymentMethodId;
    private BigDecimal amount;
    private String transactionCode;
    private String paymentStatus;
    private String note;
    private LocalDateTime paymentDate;
}