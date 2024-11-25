package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.airport.AirportDTO;
import com.example.AirplaneBooking.dto.airport.CreateAirportDTO;
import com.example.AirplaneBooking.service.AirportService;
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
@RequestMapping("/api/airports")
@RequiredArgsConstructor
@Tag(name = "Airport", description = "Airport management APIs")
public class AirportController {
    private final AirportService airportService;

    @Operation(summary = "Create a new airport", description = "Creates a new airport with the provided information")
    @ApiResponse(responseCode = "200", description = "Airport created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<AirportDTO> create(@RequestBody CreateAirportDTO createDTO) {
        return ResponseEntity.ok(airportService.create(createDTO));
    }

    @Operation(summary = "Get airport by ID", description = "Returns an airport based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Found the airport")
    @ApiResponse(responseCode = "404", description = "Airport not found")
    @GetMapping("/{id}")
    public ResponseEntity<AirportDTO> findById(
            @Parameter(description = "ID of the airport to find") @PathVariable UUID id) {
        return ResponseEntity.ok(airportService.findById(id));
    }

    @Operation(summary = "Get all airports", description = "Returns a list of all airports")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<AirportDTO>> findAll() {
        return ResponseEntity.ok(airportService.findAll());
    }

    @Operation(summary = "Update an airport", description = "Updates an airport with the provided information")
    @ApiResponse(responseCode = "200", description = "Airport updated successfully")
    @ApiResponse(responseCode = "404", description = "Airport not found")
    @PutMapping("/{id}")
    public ResponseEntity<AirportDTO> update(
            @Parameter(description = "ID of the airport to update") @PathVariable UUID id,
            @RequestBody CreateAirportDTO updateDTO) {
        return ResponseEntity.ok(airportService.update(id, updateDTO));
    }

    @Operation(summary = "Delete an airport", description = "Deletes an airport based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Airport deleted successfully")
    @ApiResponse(responseCode = "404", description = "Airport not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the airport to delete") @PathVariable UUID id) {
        airportService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Find airport by code", description = "Returns an airport based on the provided airport code")
    @ApiResponse(responseCode = "200", description = "Found the airport")
    @ApiResponse(responseCode = "404", description = "Airport not found")
    @GetMapping("/code/{airportCode}")
    public ResponseEntity<AirportDTO> findByCode(
            @Parameter(description = "Code of the airport to find") @PathVariable String airportCode) {
        return ResponseEntity.ok(airportService.findByCode(airportCode));
    }

    @Operation(summary = "Find airports by city", description = "Returns a list of airports in a specific city")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping("/city/{city}")
    public ResponseEntity<List<AirportDTO>> findByCity(
            @Parameter(description = "City name") @PathVariable String city) {
        return ResponseEntity.ok(airportService.findByCity(city));
    }

    @Operation(summary = "Find airports by country", description = "Returns a list of airports in a specific country")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping("/country/{country}")
    public ResponseEntity<List<AirportDTO>> findByCountry(
            @Parameter(description = "Country name") @PathVariable String country) {
        return ResponseEntity.ok(airportService.findByCountry(country));
    }
}