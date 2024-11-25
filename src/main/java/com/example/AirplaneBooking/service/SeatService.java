package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.seat.CreateSeatDTO;
import com.example.AirplaneBooking.dto.seat.SeatDTO;
import com.example.AirplaneBooking.model.entity.Seat;
import com.example.AirplaneBooking.repository.SeatRepository;
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
public class SeatService {
    private final SeatRepository seatRepository;
    private final ModelMapper modelMapper;

    public SeatDTO create(CreateSeatDTO createDTO) {
        Seat seat = modelMapper.map(createDTO, Seat.class);
        seat = seatRepository.save(seat);
        return modelMapper.map(seat, SeatDTO.class);
    }

    public SeatDTO findById(UUID id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));
        return modelMapper.map(seat, SeatDTO.class);
    }

    public List<SeatDTO> findByFlightId(UUID flightId) {
        return seatRepository.findByFlightId(flightId).stream()
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .collect(Collectors.toList());
    }

    public List<SeatDTO> findAvailableSeats(UUID flightId) {
        return seatRepository.findByFlightIdAndSeatStatus(flightId, "AVAILABLE").stream()
                .map(seat -> modelMapper.map(seat, SeatDTO.class))
                .collect(Collectors.toList());
    }

    public SeatDTO update(UUID id, CreateSeatDTO updateDTO) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

        modelMapper.map(updateDTO, seat);
        seat = seatRepository.save(seat);
        return modelMapper.map(seat, SeatDTO.class);
    }

    public void delete(UUID id) {
        if (!seatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seat not found");
        }
        seatRepository.deleteById(id);
    }
}