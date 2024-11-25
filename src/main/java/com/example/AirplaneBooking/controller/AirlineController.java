package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.airline.AirlineDTO;
import com.example.AirplaneBooking.dto.airline.CreateAirlineDTO;
import com.example.AirplaneBooking.service.AirlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
@Tag(name = "Airline", description = "Airline management APIs")
public class AirlineController {
    private final AirlineService airlineService;

    @Operation(summary = "Create a new airline", description = "Creates a new airline with the provided information")
    @ApiResponse(responseCode = "200", description = "Airline created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<AirlineDTO> create(@RequestBody CreateAirlineDTO createDTO) {
        return ResponseEntity.ok(airlineService.create(createDTO));
    }

    @Operation(summary = "Get airline by ID", description = "Returns an airline based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Found the airline")
    @ApiResponse(responseCode = "404", description = "Airline not found")
    @GetMapping("/{id}")
    public ResponseEntity<AirlineDTO> findById(
            @Parameter(description = "ID of the airline to find") @PathVariable UUID id) {
        return ResponseEntity.ok(airlineService.findById(id));
    }

    @Operation(summary = "Get all airlines", description = "Returns a list of all airlines")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<AirlineDTO>> findAll() {
        return ResponseEntity.ok(airlineService.findAll());
    }

    @Operation(summary = "Update an airline", description = "Updates an airline with the provided information")
    @ApiResponse(responseCode = "200", description = "Airline updated successfully")
    @ApiResponse(responseCode = "404", description = "Airline not found")
    @PutMapping("/{id}")
    public ResponseEntity<AirlineDTO> update(
            @Parameter(description = "ID of the airline to update") @PathVariable UUID id,
            @RequestBody CreateAirlineDTO updateDTO) {
        return ResponseEntity.ok(airlineService.update(id, updateDTO));
    }

    @Operation(summary = "Delete an airline", description = "Deletes an airline based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Airline deleted successfully")
    @ApiResponse(responseCode = "404", description = "Airline not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the airline to delete") @PathVariable UUID id) {
        airlineService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Find airline by code", description = "Returns an airline based on the provided airline code")
    @ApiResponse(responseCode = "200", description = "Found the airline")
    @ApiResponse(responseCode = "404", description = "Airline not found")
    @GetMapping("/code/{airlineCode}")
    public ResponseEntity<AirlineDTO> findByCode(
            @Parameter(description = "Code of the airline to find") @PathVariable String airlineCode) {
        return ResponseEntity.ok(airlineService.findByCode(airlineCode));
    }
}