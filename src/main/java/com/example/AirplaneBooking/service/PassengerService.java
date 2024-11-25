package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.passenger.CreatePassengerDTO;
import com.example.AirplaneBooking.dto.passenger.PassengerDTO;
import com.example.AirplaneBooking.model.entity.Passenger;
import com.example.AirplaneBooking.repository.PassengerRepository;
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
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final ModelMapper modelMapper;

    public PassengerDTO create(CreatePassengerDTO createDTO) {
        if (passengerRepository.findByCitizenId(createDTO.getCitizenId()).isPresent()) {
            throw new IllegalArgumentException("Citizen ID already exists");
        }

        if (createDTO.getFullName() == null || createDTO.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger name is required");
        }

        Passenger passenger = modelMapper.map(createDTO, Passenger.class);
        passenger.setFullName(createDTO.getFullName().trim());
        passenger = passengerRepository.save(passenger);
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    public PassengerDTO findById(UUID id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        System.out.println("Passenger from DB: " + passenger);
        System.out.println("Passenger name from DB: " + passenger.getFullName());

        PassengerDTO dto = modelMapper.map(passenger, PassengerDTO.class);

        System.out.println("DTO after mapping: " + dto);
        System.out.println("DTO name after mapping: " + dto.getFullName());

        return dto;
    }

    public List<PassengerDTO> findAll() {
        return passengerRepository.findAll().stream()
                .map(passenger -> modelMapper.map(passenger, PassengerDTO.class))
                .collect(Collectors.toList());
    }

    public PassengerDTO update(UUID id, CreatePassengerDTO updateDTO) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));

        if (!passenger.getCitizenId().equals(updateDTO.getCitizenId()) &&
                passengerRepository.findByCitizenId(updateDTO.getCitizenId()).isPresent()) {
            throw new IllegalArgumentException("Citizen ID already exists");
        }

        if (updateDTO.getFullName() == null || updateDTO.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger name is required");
        }

        modelMapper.map(updateDTO, passenger);
        passenger = passengerRepository.save(passenger);
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    public void delete(UUID id) {
        if (!passengerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Passenger not found");
        }
        passengerRepository.deleteById(id);
    }

    public List<PassengerDTO> findByName(String name) {
        List<Passenger> passengers = passengerRepository.findByFullNameContainingIgnoreCase(name);
        return passengers.stream()
                .map(passenger -> modelMapper.map(passenger, PassengerDTO.class))
                .collect(Collectors.toList());
    }

    public PassengerDTO findByCitizenId(String citizenId) {
        Passenger passenger = passengerRepository.findByCitizenId(citizenId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found with citizen ID: " + citizenId));
        return modelMapper.map(passenger, PassengerDTO.class);
    }
}