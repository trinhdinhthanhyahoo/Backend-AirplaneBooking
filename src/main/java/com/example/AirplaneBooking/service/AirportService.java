package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.airport.CreateAirportDTO;
import com.example.AirplaneBooking.dto.airport.AirportDTO;
import com.example.AirplaneBooking.model.entity.Airport;
import com.example.AirplaneBooking.repository.AirportRepository;
import com.example.AirplaneBooking.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;
    private final ModelMapper modelMapper;

    public AirportDTO create(CreateAirportDTO createDTO) {
        Airport airport = modelMapper.map(createDTO, Airport.class);
        airport = airportRepository.save(airport);
        return modelMapper.map(airport, AirportDTO.class);
    }

    public AirportDTO findById(UUID id) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
        return modelMapper.map(airport, AirportDTO.class);
    }

    public List<AirportDTO> findAll() {
        return airportRepository.findAll().stream()
                .map(airport -> modelMapper.map(airport, AirportDTO.class))
                .collect(Collectors.toList());
    }

    public AirportDTO update(UUID id, CreateAirportDTO updateDTO) {
        Airport airport = airportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));

        modelMapper.map(updateDTO, airport);
        airport = airportRepository.save(airport);
        return modelMapper.map(airport, AirportDTO.class);
    }

    public void delete(UUID id) {
        if (!airportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Airport not found");
        }
        airportRepository.deleteById(id);
    }

    public AirportDTO findByCode(String airportCode) {
        Airport airport = airportRepository.findByAirportCode(airportCode)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found"));
        return modelMapper.map(airport, AirportDTO.class);
    }

    public List<AirportDTO> findByCity(String city) {
        List<Airport> airports = airportRepository.findByCity(city);
        return airports.stream()
                .map(airport -> modelMapper.map(airport, AirportDTO.class))
                .collect(Collectors.toList());
    }

    public List<AirportDTO> findByCountry(String country) {
        List<Airport> airports = airportRepository.findByCountry(country);
        return airports.stream()
                .map(airport -> modelMapper.map(airport, AirportDTO.class))
                .collect(Collectors.toList());
    }
}