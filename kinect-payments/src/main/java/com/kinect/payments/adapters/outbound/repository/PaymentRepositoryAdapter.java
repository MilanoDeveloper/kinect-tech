package com.kinect.payments.adapters.outbound.repository;

import com.kinect.payments.adapters.outbound.repository.entity.PaymentEntity;
import com.kinect.payments.core.domain.Payment;
import com.kinect.payments.core.ports.outbound.PaymentRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PaymentRepositoryAdapter implements PaymentRepositoryPort {
    private final PaymentJpaRepository repository;

    public PaymentRepositoryAdapter(PaymentJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = payment.id() == null
                ? new PaymentEntity()
                : repository.findById(payment.id()).orElseThrow();
        entity.setPersonId(payment.personId());
        entity.setAmount(payment.amount());
        entity.setMethod(payment.method());
        entity.setInstallmentCount(payment.installmentCount());
        entity.setDueDate(payment.dueDate());
        entity.setPaidAt(payment.paidAt());
        entity.setStatus(payment.status() == null ? "PENDING" : payment.status());
        entity.setDescription(payment.description());
        return toDomain(repository.save(entity));
    }

    @Override
    public List<Payment> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Payment> findById(Long paymentId) {
        return repository.findById(paymentId).map(this::toDomain);
    }

    @Override
    public boolean deleteById(Long paymentId) {
        if (!repository.existsById(paymentId)) {
            return false;
        }
        repository.deleteById(paymentId);
        return true;
    }

    private Payment toDomain(PaymentEntity entity) {
        return new Payment(entity.getId(), entity.getPersonId(), entity.getAmount(),
                entity.getMethod(), entity.getInstallmentCount(), entity.getDueDate(),
                entity.getPaidAt(), entity.getStatus(), entity.getDescription(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
