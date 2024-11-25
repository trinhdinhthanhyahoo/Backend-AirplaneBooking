package com.example.AirplaneBooking.dto.paymentmethod;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodDTO {
    private String methodName;
    private String methodCode;
    private String description;
    private String status;
}