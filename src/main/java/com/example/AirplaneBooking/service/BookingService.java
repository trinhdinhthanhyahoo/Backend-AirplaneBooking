package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.booking.CreateBookingDTO;
import com.example.AirplaneBooking.dto.booking.BookingDTO;
import com.example.AirplaneBooking.model.entity.Booking;
import com.example.AirplaneBooking.repository.BookingRepository;
import com.example.AirplaneBooking.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;

    public BookingDTO create(CreateBookingDTO createDTO) {
        Booking booking = modelMapper.map(createDTO, Booking.class);
        booking.setBookingDate(LocalDateTime.now());
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDTO.class);
    }

    public BookingDTO findById(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return modelMapper.map(booking, BookingDTO.class);
    }

    public List<BookingDTO> findAll() {
        return bookingRepository.findAll().stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    public BookingDTO update(UUID id, CreateBookingDTO updateDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        modelMapper.map(updateDTO, booking);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDTO.class);
    }

    public void delete(UUID id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found");
        }
        bookingRepository.deleteById(id);
    }

    public List<BookingDTO> findByUserId(UUID userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    public BookingDTO updateStatus(UUID id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDTO.class);
    }

    public BookingDTO getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference)
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with reference: " + bookingReference));
    }

    public List<BookingDTO> findByPassengerCountGreaterThan(Integer count) {
        return bookingRepository.findByPassengerCountGreaterThan(count).stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    public List<BookingDTO> findByPassengerCountLessThan(Integer count) {
        return bookingRepository.findByPassengerCountLessThan(count).stream()
                .map(booking -> modelMapper.map(booking, BookingDTO.class))
                .collect(Collectors.toList());
    }

    public Integer getTotalPassengers() {
        return bookingRepository.countTotalPassengers();
    }
}