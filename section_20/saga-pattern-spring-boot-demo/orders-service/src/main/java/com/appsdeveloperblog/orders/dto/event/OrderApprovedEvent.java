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
public class OrderApprovedEvent {
    private UUID orderId;

    @Override
    public String toString() {
        return "OrderApprovedEvent{" +
                "orderId=" + orderId +
                '}';
    }
}
