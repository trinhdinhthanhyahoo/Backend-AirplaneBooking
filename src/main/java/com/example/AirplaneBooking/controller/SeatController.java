package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.seat.SeatDTO;
import com.example.AirplaneBooking.dto.seat.CreateSeatDTO;
import com.example.AirplaneBooking.service.SeatService;

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
@RequestMapping("/api/seats")
@Tag(name = "Seat", description = "Seat management APIs")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "Create a new seat")
    @PostMapping
    public ResponseEntity<SeatDTO> create(@Valid @RequestBody CreateSeatDTO createDTO) {
        return new ResponseEntity<>(seatService.create(createDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get seat by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(seatService.findById(id));
    }

    @Operation(summary = "Get seats by flight ID")
    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<SeatDTO>> findByFlightId(@PathVariable UUID flightId) {
        return ResponseEntity.ok(seatService.findByFlightId(flightId));
    }

    @Operation(summary = "Get available seats for flight")
    @GetMapping("/flight/{flightId}/available")
    public ResponseEntity<List<SeatDTO>> findAvailableSeats(@PathVariable UUID flightId) {
        return ResponseEntity.ok(seatService.findAvailableSeats(flightId));
    }

    @Operation(summary = "Update seat")
    @PutMapping("/{id}")
    public ResponseEntity<SeatDTO> update(@PathVariable UUID id, @Valid @RequestBody CreateSeatDTO updateDTO) {
        return ResponseEntity.ok(seatService.update(id, updateDTO));
    }

    @Operation(summary = "Delete seat")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}