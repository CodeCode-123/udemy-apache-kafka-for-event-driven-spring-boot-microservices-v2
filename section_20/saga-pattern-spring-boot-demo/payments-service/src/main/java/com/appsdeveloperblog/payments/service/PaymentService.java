package com.appsdeveloperblog.payments.service;

import java.util.List;
import com.appsdeveloperblog.payments.dto.Payment;

public interface PaymentService {
    List<Payment> findAll();

    Payment process(Payment payment);
}
