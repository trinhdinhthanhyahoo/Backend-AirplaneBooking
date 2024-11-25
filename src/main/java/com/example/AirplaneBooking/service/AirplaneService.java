package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.airplane.CreateAirplaneDTO;
import com.example.AirplaneBooking.dto.airplane.AirplaneDTO;
import com.example.AirplaneBooking.model.entity.Airplane;
import com.example.AirplaneBooking.repository.AirplaneRepository;
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
public class AirplaneService {
    private final AirplaneRepository airplaneRepository;
    private final AirlineRepository airlineRepository;
    private final ModelMapper modelMapper;

    public AirplaneDTO create(CreateAirplaneDTO createDTO) {
        if (!airlineRepository.existsById(createDTO.getAirlineId())) {
            throw new ResourceNotFoundException("Airline not found");
        }

        if (airplaneRepository.existsByAircraftCode(createDTO.getAircraftCode())) {
            throw new RuntimeException("Aircraft code already exists");
        }

        Airplane airplane = modelMapper.map(createDTO, Airplane.class);
        airplane = airplaneRepository.save(airplane);
        return modelMapper.map(airplane, AirplaneDTO.class);
    }

    public AirplaneDTO findById(UUID id) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane not found"));
        return modelMapper.map(airplane, AirplaneDTO.class);
    }

    public List<AirplaneDTO> findAll() {
        return airplaneRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AirplaneDTO convertToDTO(Airplane airplane) {
        return new AirplaneDTO(
                airplane.getAircraftId(),
                airplane.getAirlineId(),
                airplane.getAircraftCode(),
                airplane.getAircraftName(),
                airplane.getAircraftType(),
                airplane.getSeatCapacity());
    }

    public AirplaneDTO update(UUID id, CreateAirplaneDTO updateDTO) {
        Airplane airplane = airplaneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airplane not found"));

        modelMapper.map(updateDTO, airplane);
        airplane = airplaneRepository.save(airplane);
        return modelMapper.map(airplane, AirplaneDTO.class);
    }

    public void delete(UUID id) {
        if (!airplaneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Airplane not found");
        }
        airplaneRepository.deleteById(id);
    }

    public List<AirplaneDTO> findByAirlineId(UUID airlineId) {
        return airplaneRepository.findByAirlineId(airlineId).stream()
                .map(airplane -> modelMapper.map(airplane, AirplaneDTO.class))
                .collect(Collectors.toList());
    }
}