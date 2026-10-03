package com.kinect.orchestrator.application.port.out;

import com.kinect.contracts.payments.dto.Payment;
import com.kinect.contracts.payments.dto.PaymentRequest;

import java.util.List;

public interface PaymentsServicePort {
    void createPayment(PaymentRequest request);
    List<Payment> findAllPayments();
    Payment findPaymentById(Long paymentId);
    void updatePayment(Long paymentId, PaymentRequest request);
    void deletePayment(Long paymentId);
}
