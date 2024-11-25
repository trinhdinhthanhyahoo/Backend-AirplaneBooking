package com.example.AirplaneBooking.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.AirplaneBooking.model.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByBookingId(UUID bookingId);

    Optional<Payment> findByTransactionCode(String transactionCode);

    List<Payment> findByPaymentStatus(String status);

    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);
}