package com.appsdeveloperblog.orders.service.handler;

import com.appsdeveloperblog.orders.dto.command.ApproveOrderCommand;
import com.appsdeveloperblog.orders.dto.command.RejectOrderCommand;
import com.appsdeveloperblog.orders.dto.event.OrderApprovedEvent;
import com.appsdeveloperblog.orders.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = {"${orders.commands.topic.name}"})
public class OrderCommandsHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;

    public OrderCommandsHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaHandler
    public void handleCommand(ApproveOrderCommand command) {
        logger.info("ApproveOrderCommand: {}", command.toString());
        orderService.approveOrder(command.getOrderId());
    }

    @KafkaHandler
    public void handleCommand(RejectOrderCommand command) {
        logger.info("RejectOrderCommand: {}", command);
        orderService.rejectOrder(command.getOrderId());
    }
}
