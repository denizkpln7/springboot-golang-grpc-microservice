package com.denizkpln.payment_service.service;


import com.denizkpln.payment_service.model.PaymentItem;
import com.denizkpln.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentItem createPayment(PaymentItem payment) {
        return paymentRepository.save(payment);
    }
}
