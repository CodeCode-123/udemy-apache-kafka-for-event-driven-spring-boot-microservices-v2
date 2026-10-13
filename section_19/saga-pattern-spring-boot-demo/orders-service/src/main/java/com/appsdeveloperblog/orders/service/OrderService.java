package com.appsdeveloperblog.orders.service;

import com.appsdeveloperblog.orders.dto.*;

import java.util.UUID;

public interface OrderService {
    Order placeOrder(Order order);
    void approveOrder(UUID orderId);
}
