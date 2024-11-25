package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.paymentmethod.*;
import com.example.AirplaneBooking.service.PaymentMethodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment-methods")
@Tag(name = "Payment Method", description = "Payment method management APIs")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @Operation(summary = "Create a new payment method")
    @ApiResponse(responseCode = "201", description = "Payment method created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentMethodDTO> create(
            @Valid @RequestBody CreatePaymentMethodDTO createDTO) {
        return new ResponseEntity<>(paymentMethodService.create(createDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get payment method by ID")
    @ApiResponse(responseCode = "200", description = "Payment method found")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> findById(
            @Parameter(description = "Payment Method ID") @PathVariable UUID id) {
        return ResponseEntity.ok(paymentMethodService.findById(id));
    }

    @Operation(summary = "Get all active payment methods")
    @ApiResponse(responseCode = "200", description = "List of active payment methods retrieved")
    @GetMapping("/active")
    public ResponseEntity<List<PaymentMethodDTO>> findActivePaymentMethods() {
        return ResponseEntity.ok(paymentMethodService.findActivePaymentMethods());
    }

    @Operation(summary = "Update payment method")
    @ApiResponse(responseCode = "200", description = "Payment method updated successfully")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> update(
            @Parameter(description = "Payment Method ID") @PathVariable UUID id,
            @Valid @RequestBody CreatePaymentMethodDTO updateDTO) {
        return ResponseEntity.ok(paymentMethodService.update(id, updateDTO));
    }

    @Operation(summary = "Delete payment method")
    @ApiResponse(responseCode = "204", description = "Payment method deleted successfully")
    @ApiResponse(responseCode = "404", description = "Payment method not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "Payment Method ID") @PathVariable UUID id) {
        paymentMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}