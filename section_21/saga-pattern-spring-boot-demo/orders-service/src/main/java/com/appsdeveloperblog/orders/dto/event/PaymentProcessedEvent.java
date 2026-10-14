package com.appsdeveloperblog.orders.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessedEvent {
    private UUID orderId;
    private UUID paymentId;

    @Override
    public String toString() {
        return "PaymentProcessedEvent{" +
                "orderId=" + orderId +
                ", paymentId=" + paymentId +
                '}';
    }
}
