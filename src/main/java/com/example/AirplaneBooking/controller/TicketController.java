package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.ticket.TicketDTO;
import com.example.AirplaneBooking.dto.ticket.CreateTicketDTO;
import com.example.AirplaneBooking.service.TicketService;

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
@RequestMapping("/api/tickets")
@Tag(name = "Ticket", description = "Ticket management APIs")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @Operation(summary = "Create a new ticket")
    @ApiResponse(responseCode = "201", description = "Ticket created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TicketDTO> create(
            @Valid @RequestBody CreateTicketDTO createDTO) {
        return new ResponseEntity<>(ticketService.create(createDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get ticket by ID")
    @ApiResponse(responseCode = "200", description = "Ticket found")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> findById(
            @Parameter(description = "Ticket ID") @PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @Operation(summary = "Get tickets by booking ID")
    @ApiResponse(responseCode = "200", description = "List of tickets retrieved")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<TicketDTO>> findByBookingId(
            @Parameter(description = "Booking ID") @PathVariable UUID bookingId) {
        return ResponseEntity.ok(ticketService.findByBookingId(bookingId));
    }

    @Operation(summary = "Update ticket")
    @ApiResponse(responseCode = "200", description = "Ticket updated successfully")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> update(
            @Parameter(description = "Ticket ID") @PathVariable UUID id,
            @Valid @RequestBody CreateTicketDTO updateDTO) {
        return ResponseEntity.ok(ticketService.update(id, updateDTO));
    }

    @Operation(summary = "Delete ticket")
    @ApiResponse(responseCode = "204", description = "Ticket deleted successfully")
    @ApiResponse(responseCode = "404", description = "Ticket not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "Ticket ID") @PathVariable UUID id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}