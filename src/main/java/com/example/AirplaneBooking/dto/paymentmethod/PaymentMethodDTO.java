package com.example.AirplaneBooking.dto.paymentmethod;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private UUID paymentMethodId;
    private String methodName;
    private String methodCode;
    private String description;
    private String status;
}