package com.example.AirplaneBooking.service;

import com.example.AirplaneBooking.dto.paymentmethod.CreatePaymentMethodDTO;
import com.example.AirplaneBooking.dto.paymentmethod.PaymentMethodDTO;
import com.example.AirplaneBooking.model.entity.PaymentMethod;
import com.example.AirplaneBooking.repository.PaymentMethodRepository;
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
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final ModelMapper modelMapper;

    public PaymentMethodDTO create(CreatePaymentMethodDTO createDTO) {
        PaymentMethod paymentMethod = modelMapper.map(createDTO, PaymentMethod.class);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        return modelMapper.map(paymentMethod, PaymentMethodDTO.class);
    }

    public PaymentMethodDTO findById(UUID id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
        return modelMapper.map(paymentMethod, PaymentMethodDTO.class);
    }

    public List<PaymentMethodDTO> findAll() {
        return paymentMethodRepository.findAll().stream()
                .map(method -> modelMapper.map(method, PaymentMethodDTO.class))
                .collect(Collectors.toList());
    }

    public PaymentMethodDTO update(UUID id, CreatePaymentMethodDTO updateDTO) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));

        modelMapper.map(updateDTO, paymentMethod);
        paymentMethod = paymentMethodRepository.save(paymentMethod);
        return modelMapper.map(paymentMethod, PaymentMethodDTO.class);
    }

    public void delete(UUID id) {
        if (!paymentMethodRepository.existsById(id)) {
            throw new ResourceNotFoundException("Payment method not found");
        }
        paymentMethodRepository.deleteById(id);
    }

    public List<PaymentMethodDTO> findByStatus(String status) {
        return paymentMethodRepository.findByStatus(status).stream()
                .map(method -> modelMapper.map(method, PaymentMethodDTO.class))
                .collect(Collectors.toList());
    }

    public List<PaymentMethodDTO> findActivePaymentMethods() {
        List<PaymentMethod> methods = paymentMethodRepository.findAll();
        return methods.stream()
                .map(method -> modelMapper.map(method, PaymentMethodDTO.class))
                .collect(Collectors.toList());
    }
}