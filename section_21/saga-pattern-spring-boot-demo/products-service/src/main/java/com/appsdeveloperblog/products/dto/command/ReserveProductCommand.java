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
public class ReserveProductCommand {
    private UUID productId;
    private Integer productQuantity;
    private UUID orderId;

    @Override
    public String toString() {
        return "ReserveProductCommand{" +
                "productId=" + productId +
                ", productQuantity=" + productQuantity +
                ", orderId=" + orderId +
                '}';
    }
}
