package com.kinect.payments.core.ports.inbound;

import com.kinect.payments.core.domain.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentUseCase {
    Payment create(Payment payment);
    List<Payment> findAll();
    Optional<Payment> findById(Long paymentId);
    Optional<Payment> update(Long paymentId, Payment payment);
    boolean delete(Long paymentId);
}
