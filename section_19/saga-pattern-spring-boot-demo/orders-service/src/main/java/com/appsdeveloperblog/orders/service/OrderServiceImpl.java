package com.appsdeveloperblog.orders.service;

import com.appsdeveloperblog.orders.dao.jpa.entity.OrderEntity;
import com.appsdeveloperblog.orders.dao.jpa.repository.OrderRepository;
import com.appsdeveloperblog.orders.dto.Order;
import com.appsdeveloperblog.orders.types.OrderStatus;

import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;

	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Override
	public Order placeOrder(Order order) {
		OrderEntity entity = new OrderEntity();
		entity.setCustomerId(order.getCustomerId());
		entity.setProductId(order.getProductId());
		entity.setProductQuantity(order.getProductQuantity());
		entity.setStatus(OrderStatus.CREATED);
		orderRepository.save(entity);

		return new Order(entity.getId(), entity.getCustomerId(), entity.getProductId(), entity.getProductQuantity(),
				entity.getStatus());
	}

}
