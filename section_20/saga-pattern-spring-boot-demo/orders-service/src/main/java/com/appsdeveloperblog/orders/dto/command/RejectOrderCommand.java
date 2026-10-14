package com.appsdeveloperblog.orders.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RejectOrderCommand {
    private UUID orderId;

    @Override
    public String toString() {
        return "RejectOrderCommand{" +
                "orderId=" + orderId +
                '}';
    }
}
