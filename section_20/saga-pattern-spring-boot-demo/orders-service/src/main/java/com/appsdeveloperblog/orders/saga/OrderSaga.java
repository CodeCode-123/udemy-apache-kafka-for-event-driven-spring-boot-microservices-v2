package com.appsdeveloperblog.orders.saga;

import com.appsdeveloperblog.orders.dto.command.ApproveOrderCommand;
import com.appsdeveloperblog.orders.dto.command.ProcessPaymentCommand;
import com.appsdeveloperblog.orders.dto.command.ReserveProductCommand;
import com.appsdeveloperblog.orders.dto.event.*;
import com.appsdeveloperblog.orders.service.OrderHistoryService;
import com.appsdeveloperblog.orders.types.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@KafkaListener(topics = {
        "${orders.events.topic.name}",
        "${products.events.topic.name}",
        "${payments.events.topic.name}"})
public class OrderSaga {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productsCommandsTopicName;
    private final String paymentsCommandsTopicName;
    private final String ordersCommandsTopicName;
    private final OrderHistoryService orderHistoryService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public OrderSaga(KafkaTemplate<String, Object> kafkaTemplate,
                     @Value("${products.commands.topic.name}") String productsCommandsTopicName,
                     @Value("${payments.commands.topic.name}") String paymentsCommandsTopicName,
                     @Value("${orders.commands.topic.name}") String ordersCommandsTopicName,
                     OrderHistoryService orderHistoryService) {
        this.kafkaTemplate = kafkaTemplate;
        this.productsCommandsTopicName = productsCommandsTopicName;
        this.paymentsCommandsTopicName = paymentsCommandsTopicName;
        this.ordersCommandsTopicName = ordersCommandsTopicName;
        this.orderHistoryService = orderHistoryService;
    }

    @KafkaHandler
    public void handleEvent(OrderCreatedEvent event) {
        ReserveProductCommand command = new ReserveProductCommand(
                event.getProductId(),
                event.getProductQuantity(),
                event.getOrderId()
        );
        kafkaTemplate.send(productsCommandsTopicName, command);
        orderHistoryService.add(event.getOrderId(), OrderStatus.CREATED);
    }

    @KafkaHandler
    public void handleEvent(ProductReservedEvent event) {
        logger.info("ProductReservedEvent: {}", event.toString());
        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(
                event.getOrderId(),
                event.getProductId(),
                event.getProductPrice(),
                event.getProductQuantity()
        );
        kafkaTemplate.send(paymentsCommandsTopicName, processPaymentCommand);
    }

    @KafkaHandler
    public void handleEvent(ProductReservationFailedEvent event) {
        logger.info("ProductReservationFailedEvent: {}", event.toString());
    }

    @KafkaHandler
    public void handleEvent(PaymentProcessedEvent event) {
        logger.info("PaymentProcessedEvent: {}", event.toString());
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(event.getOrderId());
        kafkaTemplate.send(ordersCommandsTopicName, approveOrderCommand);
    }

    @KafkaHandler
    public void handleEvent(PaymentFailedEvent event) {
        logger.info("PaymentFailedEvent: {}", event.toString());
    }

    @KafkaHandler
    public void handleEvent(OrderApprovedEvent event) {
        logger.info("OrderApprovedEvent: {}", event);
        orderHistoryService.add(event.getOrderId(), OrderStatus.APPROVED);
    }
}
