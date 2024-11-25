package com.example.AirplaneBooking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.example.AirplaneBooking.dto.passenger.*;
import com.example.AirplaneBooking.service.PassengerService;

@RestController
@RequestMapping("/api/passengers")
@Tag(name = "Passenger", description = "Passenger management APIs")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @Operation(summary = "Create a new passenger")
    @ApiResponse(responseCode = "201", description = "Passenger created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PassengerDTO> create(@Valid @RequestBody CreatePassengerDTO createDTO) {
        return new ResponseEntity<>(passengerService.create(createDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get passenger by ID")
    @ApiResponse(responseCode = "200", description = "Found the passenger")
    @ApiResponse(responseCode = "404", description = "Passenger not found")
    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> findById(
            @Parameter(description = "ID of the passenger") @PathVariable UUID id) {
        return ResponseEntity.ok(passengerService.findById(id));
    }

    @Operation(summary = "Update a passenger")
    @ApiResponse(responseCode = "200", description = "Passenger updated successfully")
    @ApiResponse(responseCode = "404", description = "Passenger not found")
    @PutMapping("/{id}")
    public ResponseEntity<PassengerDTO> update(
            @Parameter(description = "ID of the passenger") @PathVariable UUID id,
            @Valid @RequestBody CreatePassengerDTO updateDTO) {
        return ResponseEntity.ok(passengerService.update(id, updateDTO));
    }

    @Operation(summary = "Delete a passenger")
    @ApiResponse(responseCode = "204", description = "Passenger deleted successfully")
    @ApiResponse(responseCode = "404", description = "Passenger not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the passenger") @PathVariable UUID id) {
        passengerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get passengers by name")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping("/search")
    public ResponseEntity<List<PassengerDTO>> findByName(
            @Parameter(description = "Name to search") @RequestParam String name) {
        return ResponseEntity.ok(passengerService.findByName(name));
    }

    @Operation(summary = "Get passenger by citizen ID")
    @ApiResponse(responseCode = "200", description = "Found the passenger")
    @ApiResponse(responseCode = "404", description = "Passenger not found")
    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<PassengerDTO> findByCitizenId(
            @Parameter(description = "Citizen ID of the passenger") @PathVariable String citizenId) {
        return ResponseEntity.ok(passengerService.findByCitizenId(citizenId));
    }
}