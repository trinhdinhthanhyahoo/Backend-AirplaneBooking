package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.ticket.CreateTicketDTO;
import com.example.AirplaneBooking.dto.ticket.TicketDTO;
import com.example.AirplaneBooking.model.entity.Ticket;
import com.example.AirplaneBooking.repository.TicketRepository;
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
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    public TicketDTO create(CreateTicketDTO createDTO) {
        Ticket ticket = modelMapper.map(createDTO, Ticket.class);
        ticket = ticketRepository.save(ticket);
        return modelMapper.map(ticket, TicketDTO.class);
    }

    public TicketDTO findById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        return modelMapper.map(ticket, TicketDTO.class);
    }

    public List<TicketDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketDTO.class))
                .collect(Collectors.toList());
    }

    public TicketDTO update(UUID id, CreateTicketDTO updateDTO) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        modelMapper.map(updateDTO, ticket);
        ticket = ticketRepository.save(ticket);
        return modelMapper.map(ticket, TicketDTO.class);
    }

    public void delete(UUID id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }

    public List<TicketDTO> findByBookingId(UUID bookingId) {
        return ticketRepository.findByBookingId(bookingId).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDTO.class))
                .collect(Collectors.toList());
    }

    public List<TicketDTO> findByPassengerId(UUID passengerId) {
        return ticketRepository.findByPassengerId(passengerId).stream()
                .map(ticket -> modelMapper.map(ticket, TicketDTO.class))
                .collect(Collectors.toList());
    }

    public TicketDTO updateStatus(UUID id, String status) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        ticket.setStatus(status);
        ticket = ticketRepository.save(ticket);
        return modelMapper.map(ticket, TicketDTO.class);
    }
}