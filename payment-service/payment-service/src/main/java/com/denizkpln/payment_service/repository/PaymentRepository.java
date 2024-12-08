package com.denizkpln.payment_service.repository;

import com.denizkpln.payment_service.model.PaymentItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentItem, Long> {
}
