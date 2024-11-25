package com.example.AirplaneBooking.model.entity;

import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payment_method")
@Data
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "method_id")
    private UUID paymentMethodId;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "method_code")
    private String methodCode;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;
}