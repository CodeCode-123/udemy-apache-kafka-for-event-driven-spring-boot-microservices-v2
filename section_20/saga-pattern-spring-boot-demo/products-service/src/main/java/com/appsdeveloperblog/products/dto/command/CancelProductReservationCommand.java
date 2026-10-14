package com.appsdeveloperblog.products.dto.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelProductReservationCommand {
    private UUID productId;
    private UUID orderId;
    private Integer productQuantity;

    @Override
    public String toString() {
        return "CancelProductReservationCommand{" +
                "productId=" + productId +
                ", orderId=" + orderId +
                ", productQuantity=" + productQuantity +
                '}';
    }
}
