package com.kinect.payments.adapters.outbound.repository;

import com.kinect.payments.adapters.outbound.repository.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {
}
