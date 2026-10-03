package com.kinect.payments.core.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record Payment(
        Long id,
        Long personId,
        BigDecimal amount,
        String method,
        Integer installmentCount,
        LocalDate dueDate,
        OffsetDateTime paidAt,
        String status,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}
