package com.kinect.payments.core.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Payment(
        Long id,
        Long personId,
        BigDecimal amount,
        String method,
        Integer installmentCount,
        LocalDate dueDate,
        LocalDate paidAt,
        String status,
        String description,
        LocalDate createdAt,
        LocalDate updatedAt) {
}
