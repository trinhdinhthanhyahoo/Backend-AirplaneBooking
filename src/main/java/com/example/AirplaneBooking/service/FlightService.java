package com.example.AirplaneBooking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

import com.example.AirplaneBooking.model.entity.Flight;
import com.example.AirplaneBooking.dto.flight.CreateFlightDTO;
import com.example.AirplaneBooking.dto.flight.FlightDTO;

import com.example.AirplaneBooking.repository.FlightRepository;
import com.example.AirplaneBooking.repository.BookingRepository;
import com.example.AirplaneBooking.repository.SeatRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.AirplaneBooking.exception.DuplicateResourceException;
import com.example.AirplaneBooking.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AirplaneBooking.model.enums.FlightStatus;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SeatRepository seatRepository;
    private final ModelMapper modelMapper;

    public FlightDTO create(CreateFlightDTO createDTO) {
        if (flightRepository.existsByFlightCode(createDTO.getFlightCode())) {
            throw new DuplicateResourceException("Flight code already exists");
        }
        Flight flight = modelMapper.map(createDTO, Flight.class);
        flight.setAvailableSeats(flight.getAircraft().getTotalSeats());
        flight = flightRepository.save(flight);
        return modelMapper.map(flight, FlightDTO.class);
    }

    public FlightDTO findById(UUID id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
        return modelMapper.map(flight, FlightDTO.class);
    }

    private FlightDTO convertToDTO(Flight flight) {
        log.info("Converting flight to DTO: {}", flight);

        return FlightDTO.builder()
                .flightId(flight.getFlightId())
                .flightCode(flight.getFlightCode())
                .aircraftId(flight.getAircraft() != null ? flight.getAircraft().getAircraftId() : null)
                .departureAirportId(
                        flight.getDepartureAirport() != null ? flight.getDepartureAirport().getAirportId() : null)
                .arrivalAirportId(flight.getArrivalAirport() != null ? flight.getArrivalAirport().getAirportId() : null)
                .departureDateTime(flight.getDepartureDateTime())
                .arrivalDateTime(flight.getArrivalDateTime())
                .baseFare(flight.getBaseFare())
                .availableSeats(flight.getAvailableSeats())
                .status(flight.getStatus())
                .build();
    }

    public List<FlightDTO> findAll() {
        return flightRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public FlightDTO update(UUID id, CreateFlightDTO updateDTO) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
        modelMapper.map(updateDTO, flight);
        flight = flightRepository.save(flight);
        return modelMapper.map(flight, FlightDTO.class);
    }

    @Transactional
    public void delete(UUID id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        // Delete associated seats first
        seatRepository.deleteByFlightId(id);

        // Delete associated bookings
        bookingRepository.deleteByFlight_FlightId(id);

        // Finally delete the flight
        flightRepository.delete(flight);
        flightRepository.flush();
    }

    public List<FlightDTO> searchFlights(UUID departureAirportId, UUID arrivalAirportId, LocalDate departureDate) {
        LocalDateTime startDateTime = departureDate.atStartOfDay();
        LocalDateTime endDateTime = departureDate.plusDays(1).atStartOfDay();
        return flightRepository
                .findByAirportsAndDateRange(departureAirportId, arrivalAirportId, startDateTime, endDateTime)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updateAvailableSeats(UUID flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        Integer bookedSeats = flightRepository.countBookedSeatsByFlightId(flightId);
        flight.updateAvailableSeats(bookedSeats);
        flightRepository.save(flight);
    }

    @Transactional
    public FlightDTO updateStatus(UUID id, FlightStatus status) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id: " + id));

        // Add validation if needed
        if (status == FlightStatus.CANCELLED) {
            // You might want to handle cancellations specially
            // For example, notify passengers, handle refunds, etc.
        }

        flight.setStatus(status);
        flight = flightRepository.save(flight);
        log.info("Updated status of flight {} to {}", id, status);
        return convertToDTO(flight);
    }

    public List<FlightDTO> findByStatus(FlightStatus status) {
        return flightRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}