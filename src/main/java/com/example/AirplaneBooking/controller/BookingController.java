package com.example.AirplaneBooking.controller;

import com.example.AirplaneBooking.dto.booking.BookingDTO;
import com.example.AirplaneBooking.dto.booking.CreateBookingDTO;
import com.example.AirplaneBooking.service.BookingService;
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
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Booking management APIs")
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Create a new booking", description = "Creates a new booking with the provided information")
    @ApiResponse(responseCode = "200", description = "Booking created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @PostMapping
    public ResponseEntity<BookingDTO> create(@RequestBody CreateBookingDTO createDTO) {
        return ResponseEntity.ok(bookingService.create(createDTO));
    }

    @Operation(summary = "Get booking by ID", description = "Returns a booking based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Found the booking")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findById(
            @Parameter(description = "ID of the booking to find") @PathVariable UUID id) {
        return ResponseEntity.ok(bookingService.findById(id));
    }

    @Operation(summary = "Get all bookings", description = "Returns a list of all bookings")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping
    public ResponseEntity<List<BookingDTO>> findAll() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @Operation(summary = "Update a booking", description = "Updates a booking with the provided information")
    @ApiResponse(responseCode = "200", description = "Booking updated successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> update(
            @Parameter(description = "ID of the booking to update") @PathVariable UUID id,
            @RequestBody CreateBookingDTO updateDTO) {
        return ResponseEntity.ok(bookingService.update(id, updateDTO));
    }

    @Operation(summary = "Delete a booking", description = "Deletes a booking based on the provided ID")
    @ApiResponse(responseCode = "200", description = "Booking deleted successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the booking to delete") @PathVariable UUID id) {
        bookingService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Find bookings by user", description = "Returns a list of bookings for a specific user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> findByUserId(
            @Parameter(description = "ID of the user") @PathVariable UUID userId) {
        return ResponseEntity.ok(bookingService.findByUserId(userId));
    }

    @Operation(summary = "Update booking status", description = "Updates the status of a booking")
    @ApiResponse(responseCode = "200", description = "Status updated successfully")
    @ApiResponse(responseCode = "404", description = "Booking not found")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingDTO> updateStatus(
            @Parameter(description = "ID of the booking") @PathVariable UUID id,
            @Parameter(description = "New status") @RequestParam String status) {
        return ResponseEntity.ok(bookingService.updateStatus(id, status));
    }

    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<BookingDTO> getBookingByReference(
            @PathVariable String bookingReference) {
        BookingDTO booking = bookingService.getBookingByReference(bookingReference);
        return ResponseEntity.ok(booking);
    }

    @Operation(summary = "Get bookings by minimum passenger count", description = "Returns bookings with passenger count greater than specified number")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping("/passengers/min/{count}")
    public ResponseEntity<List<BookingDTO>> findByMinPassengers(
            @Parameter(description = "Minimum number of passengers") @PathVariable Integer count) {
        return ResponseEntity.ok(bookingService.findByPassengerCountGreaterThan(count));
    }

    @Operation(summary = "Get bookings by maximum passenger count", description = "Returns bookings with passenger count less than specified number")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    @GetMapping("/passengers/max/{count}")
    public ResponseEntity<List<BookingDTO>> findByMaxPassengers(
            @Parameter(description = "Maximum number of passengers") @PathVariable Integer count) {
        return ResponseEntity.ok(bookingService.findByPassengerCountLessThan(count));
    }

    @Operation(summary = "Get total passenger count", description = "Returns the total number of passengers across all bookings")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved total")
    @GetMapping("/passengers/total")
    public ResponseEntity<Integer> getTotalPassengers() {
        return ResponseEntity.ok(bookingService.getTotalPassengers());
    }
}