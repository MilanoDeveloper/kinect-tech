package com.kinect.payments.application;

import com.kinect.payments.core.domain.Payment;
import com.kinect.payments.core.ports.inbound.PaymentUseCase;
import com.kinect.payments.core.ports.outbound.PaymentRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements PaymentUseCase {
    private final PaymentRepositoryPort repository;

    public PaymentService(PaymentRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Payment create(Payment payment) {
        return repository.save(payment);
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Payment> findById(Long paymentId) {
        return repository.findById(paymentId);
    }

    @Override
    public Optional<Payment> update(Long paymentId, Payment payment) {
        if (repository.findById(paymentId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(repository.save(new Payment(
                paymentId, payment.personId(), payment.amount(), payment.method(),
                payment.installmentCount(), payment.dueDate(), payment.paidAt(),
                payment.status(), payment.description(), null, null)));
    }

    @Override
    public boolean delete(Long paymentId) {
        return repository.deleteById(paymentId);
    }
}
