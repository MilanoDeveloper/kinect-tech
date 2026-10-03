package com.kinect.orchestrator.adapters.inbound.controller;

import com.kinect.contracts.payments.api.PaymentsApi;
import com.kinect.contracts.payments.dto.Payment;
import com.kinect.contracts.payments.dto.PaymentRequest;
import com.kinect.orchestrator.application.port.in.GymOperationsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentsController implements PaymentsApi {
    private final GymOperationsUseCase useCase;

    public PaymentsController(GymOperationsUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public ResponseEntity<Void> createPayment(PaymentRequest request) {
        useCase.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<Payment>> getPayments() {
        return ResponseEntity.ok(useCase.findPayments());
    }

    @Override
    public ResponseEntity<Payment> getPaymentById(Long paymentId) {
        return ResponseEntity.ok(useCase.findPayment(paymentId));
    }

    @Override
    public ResponseEntity<Void> updatePayment(Long paymentId, PaymentRequest request) {
        useCase.updatePayment(paymentId, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deletePayment(Long paymentId) {
        useCase.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }
}
