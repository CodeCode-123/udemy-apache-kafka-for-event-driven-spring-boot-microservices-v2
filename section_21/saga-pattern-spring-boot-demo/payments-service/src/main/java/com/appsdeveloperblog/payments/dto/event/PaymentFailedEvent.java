package com.appsdeveloperblog.payments.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFailedEvent {
    private UUID orderId;
    private UUID productId;
    private Integer productQuantity;

    @Override
    public String toString() {
        return "PaymentFailedEvent{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", productQuantity=" + productQuantity +
                '}';
    }
}
