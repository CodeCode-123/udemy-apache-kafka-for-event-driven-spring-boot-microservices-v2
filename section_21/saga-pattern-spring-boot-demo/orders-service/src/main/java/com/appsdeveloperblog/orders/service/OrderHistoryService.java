package com.appsdeveloperblog.orders.service;


import com.appsdeveloperblog.orders.dto.OrderHistory;
import com.appsdeveloperblog.orders.types.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderHistoryService {
    void add(UUID orderId, OrderStatus orderStatus);

    List<OrderHistory> findByOrderId(UUID orderId);
}
