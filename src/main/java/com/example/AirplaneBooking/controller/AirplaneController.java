package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.airplane.AirplaneDTO;
import com.example.AirplaneBooking.dto.airplane.CreateAirplaneDTO;
import com.example.AirplaneBooking.service.AirplaneService;

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
@RequestMapping("/api/airplanes")
@Tag(name = "Airplane", description = "Airplane management APIs")
@RequiredArgsConstructor
public class AirplaneController {
    private final AirplaneService airplaneService;

    @Operation(summary = "Create a new airplane")
    @ApiResponse(responseCode = "201", description = "Airplane created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AirplaneDTO> create(
            @Valid @RequestBody CreateAirplaneDTO createDTO) {
        return new ResponseEntity<>(airplaneService.create(createDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Get airplane by ID")
    @ApiResponse(responseCode = "200", description = "Airplane found")
    @ApiResponse(responseCode = "404", description = "Airplane not found")
    @GetMapping("/{id}")
    public ResponseEntity<AirplaneDTO> findById(
            @Parameter(description = "Airplane ID") @PathVariable UUID id) {
        return ResponseEntity.ok(airplaneService.findById(id));
    }

    @Operation(summary = "Get all airplanes")
    @ApiResponse(responseCode = "200", description = "List of airplanes retrieved")
    @GetMapping
    public ResponseEntity<List<AirplaneDTO>> findAll() {
        return ResponseEntity.ok(airplaneService.findAll());
    }

    @Operation(summary = "Update airplane")
    @ApiResponse(responseCode = "200", description = "Airplane updated successfully")
    @ApiResponse(responseCode = "404", description = "Airplane not found")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PutMapping("/{id}")
    public ResponseEntity<AirplaneDTO> update(
            @Parameter(description = "Airplane ID") @PathVariable UUID id,
            @Valid @RequestBody CreateAirplaneDTO updateDTO) {
        return ResponseEntity.ok(airplaneService.update(id, updateDTO));
    }

    @Operation(summary = "Delete airplane")
    @ApiResponse(responseCode = "204", description = "Airplane deleted successfully")
    @ApiResponse(responseCode = "404", description = "Airplane not found")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "Airplane ID") @PathVariable UUID id) {
        airplaneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}