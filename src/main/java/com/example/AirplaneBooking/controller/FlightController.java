package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.flight.FlightDTO;
import com.example.AirplaneBooking.dto.flight.CreateFlightDTO;
import com.example.AirplaneBooking.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
@Tag(name = "Flight", description = "Flight management APIs")
@Slf4j
public class FlightController {
    private final FlightService flightService;

    @Operation(summary = "Create a new flight", description = "Creates a new flight with the provided information")
    @ApiResponse(responseCode = "200", description = "Flight created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<FlightDTO> create(@RequestBody CreateFlightDTO createDTO) {
        return ResponseEntity.ok(flightService.create(createDTO));
    }

    @Operation(summary = "Get flight by ID", description = "Returns a flight based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Found the flight")
    @ApiResponse(responseCode = "404", description = "Flight not found")
    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> findById(
            @Parameter(description = "ID of the flight to find") @PathVariable UUID id) {
        return ResponseEntity.ok(flightService.findById(id));
    }

    @Operation(summary = "Get all flights", description = "Returns a list of all flights")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<FlightDTO>> findAll() {
        return ResponseEntity.ok(flightService.findAll());
    }

    @Operation(summary = "Update a flight", description = "Updates a flight with the provided information")
    @ApiResponse(responseCode = "200", description = "Flight updated successfully")
    @ApiResponse(responseCode = "404", description = "Flight not found")
    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> update(
            @Parameter(description = "ID of the flight to update") @PathVariable UUID id,
            @RequestBody CreateFlightDTO updateDTO) {
        return ResponseEntity.ok(flightService.update(id, updateDTO));
    }

    @Operation(summary = "Delete a flight", description = "Deletes a flight based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Flight deleted successfully")
    @ApiResponse(responseCode = "404", description = "Flight not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the flight to delete") @PathVariable UUID id) {
        flightService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get available seats", description = "Returns the number of available seats for a flight")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available seats")
    @ApiResponse(responseCode = "404", description = "Flight not found")
    @GetMapping("/{id}/available-seats")
    public ResponseEntity<Integer> getAvailableSeats(
            @Parameter(description = "ID of the flight") @PathVariable UUID id) {
        FlightDTO flight = flightService.findById(id);
        return ResponseEntity.ok(flight.getAvailableSeats());
    }

}