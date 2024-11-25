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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.AirplaneBooking.exception.DuplicateResourceException;
import com.example.AirplaneBooking.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FlightService {
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private BookingRepository bookingRepository;
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

        bookingRepository.deleteById(id);

        flightRepository.delete(flight);
        flightRepository.flush(); // Đảm bảo thay đổi được ghi vào DB ngay lập tức
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
}