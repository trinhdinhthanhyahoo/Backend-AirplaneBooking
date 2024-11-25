package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.airline.AirlineDTO;
import com.example.AirplaneBooking.dto.airline.CreateAirlineDTO;
import com.example.AirplaneBooking.model.entity.Airline;
import com.example.AirplaneBooking.repository.AirlineRepository;
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
public class AirlineService {
    private final AirlineRepository airlineRepository;
    private final ModelMapper modelMapper;

    public AirlineDTO create(CreateAirlineDTO createDTO) {
        Airline airline = modelMapper.map(createDTO, Airline.class);
        airline = airlineRepository.save(airline);
        return modelMapper.map(airline, AirlineDTO.class);
    }

    public AirlineDTO findById(UUID id) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));
        return modelMapper.map(airline, AirlineDTO.class);
    }

    public List<AirlineDTO> findAll() {
        return airlineRepository.findAll().stream()
                .map(airline -> modelMapper.map(airline, AirlineDTO.class))
                .collect(Collectors.toList());
    }

    public AirlineDTO update(UUID id, CreateAirlineDTO updateDTO) {
        Airline airline = airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));

        modelMapper.map(updateDTO, airline);
        airline = airlineRepository.save(airline);
        return modelMapper.map(airline, AirlineDTO.class);
    }

    public void delete(UUID id) {
        if (!airlineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Airline not found");
        }
        airlineRepository.deleteById(id);
    }

    public AirlineDTO findByCode(String airlineCode) {
        Airline airline = airlineRepository.findByAirlineCode(airlineCode)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found"));
        return modelMapper.map(airline, AirlineDTO.class);
    }
}