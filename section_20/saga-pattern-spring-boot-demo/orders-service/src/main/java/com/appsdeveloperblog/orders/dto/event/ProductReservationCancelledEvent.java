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
public class ProductReservationCancelledEvent {
    private UUID productId;
    private UUID orderId;

    @Override
    public String toString() {
        return "ProductReservationCancelledEvent{" +
                "productId=" + productId +
                ", orderId=" + orderId +
                '}';
    }
}
