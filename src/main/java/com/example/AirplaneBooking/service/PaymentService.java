package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.payment.CreatePaymentDTO;
import com.example.AirplaneBooking.dto.payment.PaymentDTO;
import com.example.AirplaneBooking.model.entity.Payment;
import com.example.AirplaneBooking.repository.PaymentRepository;
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
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    public PaymentDTO create(CreatePaymentDTO createDTO) {
        Payment payment = modelMapper.map(createDTO, Payment.class);
        payment.setPaymentDate(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO findById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public List<PaymentDTO> findAll() {
        return paymentRepository.findAll().stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    public PaymentDTO update(UUID id, CreatePaymentDTO updateDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        modelMapper.map(updateDTO, payment);
        payment = paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public void delete(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment not found");
        }
        paymentRepository.deleteById(id);
    }

    public List<PaymentDTO> findByBookingId(UUID bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .collect(Collectors.toList());
    }

    public PaymentDTO updateStatus(UUID id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        payment.setPaymentStatus(status);
        payment = paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO findByTransactionCode(String transactionCode) {
        Payment payment = paymentRepository.findByTransactionCode(transactionCode)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Payment not found with transaction code: " + transactionCode
            ));
        
        return modelMapper.map(payment, PaymentDTO.class);
    }
}