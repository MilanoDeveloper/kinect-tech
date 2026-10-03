package com.kinect.payments.core.ports.outbound;

import com.kinect.payments.core.domain.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryPort {
    Payment save(Payment payment);
    List<Payment> findAll();
    Optional<Payment> findById(Long paymentId);
    boolean deleteById(Long paymentId);
}
