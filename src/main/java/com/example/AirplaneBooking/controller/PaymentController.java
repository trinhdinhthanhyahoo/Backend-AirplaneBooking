package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.payment.PaymentDTO;
import com.example.AirplaneBooking.dto.payment.CreatePaymentDTO;
import com.example.AirplaneBooking.service.PaymentService;

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
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "Payment management APIs")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Create a new payment")
    @ApiResponse(responseCode = "201", description = "Payment created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PaymentDTO> create(@Valid @RequestBody CreatePaymentDTO createDTO) {
        return new ResponseEntity<>(paymentService.create(createDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get payment by ID")
    @ApiResponse(responseCode = "200", description = "Payment found")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findById(
            @Parameter(description = "Payment ID") @PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @Operation(summary = "Get payments by booking ID")
    @ApiResponse(responseCode = "200", description = "List of payments retrieved")
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentDTO>> findByBookingId(
            @Parameter(description = "Booking ID") @PathVariable UUID bookingId) {
        return ResponseEntity.ok(paymentService.findByBookingId(bookingId));
    }

    @Operation(summary = "Get payment by transaction code")
    @ApiResponse(responseCode = "200", description = "Payment found")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @GetMapping("/transaction/{transactionCode}")
    public ResponseEntity<PaymentDTO> findByTransactionCode(
            @Parameter(description = "Transaction Code") @PathVariable String transactionCode) {
        return ResponseEntity.ok(paymentService.findByTransactionCode(transactionCode));
    }

    @Operation(summary = "Update payment")
    @ApiResponse(responseCode = "200", description = "Payment updated successfully")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(
            @Parameter(description = "Payment ID") @PathVariable UUID id,
            @Valid @RequestBody CreatePaymentDTO updateDTO) {
        return ResponseEntity.ok(paymentService.update(id, updateDTO));
    }

    @Operation(summary = "Delete payment")
    @ApiResponse(responseCode = "204", description = "Payment deleted successfully")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "Payment ID") @PathVariable UUID id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}