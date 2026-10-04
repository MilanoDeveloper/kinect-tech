package com.kinect.payments.adapters.inbound.controller;

import com.kinect.contracts.payments.api.PaymentsApi;
import com.kinect.contracts.payments.dto.Payment;
import com.kinect.contracts.payments.dto.PaymentRequest;
import com.kinect.payments.core.ports.inbound.PaymentUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class PaymentController implements PaymentsApi {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final PaymentUseCase useCase;

    public PaymentController(PaymentUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public ResponseEntity<Void> createPayment(PaymentRequest request) {
        useCase.create(toDomain(request, null));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<Payment>> getPayments() {
        return ResponseEntity.ok(useCase.findAll().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<Payment> getPaymentById(Long paymentId) {
        return ResponseEntity.ok(useCase.findById(paymentId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found")));
    }

    @Override
    public ResponseEntity<Void> updatePayment(Long paymentId, PaymentRequest request) {
        if (useCase.update(paymentId, toDomain(request, paymentId)).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deletePayment(Long paymentId) {
        if (!useCase.delete(paymentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        return ResponseEntity.noContent().build();
    }

    private com.kinect.payments.core.domain.Payment toDomain(PaymentRequest request, Long id) {
        return new com.kinect.payments.core.domain.Payment(
                id, request.getPersonId(), request.getAmount(), request.getMethod().getValue(),
                request.getInstallmentCount(), request.getDueDate(), request.getPaidAt(),
                request.getStatus() == null ? "PENDING" : request.getStatus().getValue(),
                request.getDescription(), null, null);
    }

    private Payment toResponse(com.kinect.payments.core.domain.Payment payment) {
        Payment response = new Payment();
        response.setId(payment.id());
        response.setPersonId(payment.personId());
        response.setAmount(payment.amount());
        response.setMethod(Payment.MethodEnum.fromValue(payment.method()));
        response.setInstallmentCount(payment.installmentCount());
        response.setDueDate(formatDate(payment.dueDate()));
        response.setPaidAt(formatDate(payment.paidAt()));
        response.setStatus(Payment.StatusEnum.fromValue(payment.status()));
        response.setDescription(payment.description());
        response.setCreatedAt(formatDate(payment.createdAt()));
        response.setUpdatedAt(formatDate(payment.updatedAt()));
        return response;
    }

    private String formatDate(LocalDate date) {
        return date == null ? null : date.format(DATE_FORMAT);
    }
}
