package com.appsdeveloperblog.products.service.handler;

import com.appsdeveloperblog.products.dto.Product;
import com.appsdeveloperblog.products.dto.command.CancelProductReservationCommand;
import com.appsdeveloperblog.products.dto.command.ReserveProductCommand;
import com.appsdeveloperblog.products.dto.event.ProductReservationCancelledEvent;
import com.appsdeveloperblog.products.dto.event.ProductReservationFailedEvent;
import com.appsdeveloperblog.products.dto.event.ProductReservedEvent;
import com.appsdeveloperblog.products.exceptions.ProductInsufficientQuantityException;
import com.appsdeveloperblog.products.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@KafkaListener(topics = "${products.commands.topic.name}")
public class ProductCommandsHandler {

    private final ProductService productService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final String productEventsTopicName;

    public ProductCommandsHandler(ProductService productService,
                                  KafkaTemplate<String, Object> kafkaTemplate,
                                  @Value("${products.events.topic.name}") String productEventsTopicName) {
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
        this.productEventsTopicName = productEventsTopicName;
    }

    @KafkaHandler
    public void handleCommand(ReserveProductCommand command) {
        logger.info("ReserveProductCommand: {}", command.toString());
        try {
            Product desiredProduct = new Product(command.getProductId(), command.getProductQuantity());
            Product reservedProduct = productService.reserve(desiredProduct, command.getOrderId());
            ProductReservedEvent productReservedEvent = new ProductReservedEvent(
                    command.getOrderId(),
                    command.getProductId(),
                    reservedProduct.getPrice(),
                    command.getProductQuantity());
            kafkaTemplate.send(productEventsTopicName, productReservedEvent);
        } catch (ProductInsufficientQuantityException e) {
            logger.error(e.getLocalizedMessage(), e);
            ProductReservationFailedEvent productReservationFailedEvent = new ProductReservationFailedEvent(
                    command.getOrderId(),
                    command.getProductId(),
                    command.getProductQuantity()
            );
            kafkaTemplate.send(productEventsTopicName, productReservationFailedEvent);
        }
    }

    @KafkaHandler
    public void handleCommand(CancelProductReservationCommand command) {
        logger.info("CancelProductReservationCommand: {}", command.toString());
        Product productToCancel = new Product(command.getProductId(), command.getProductQuantity());
        productService.cancelReservation(productToCancel, command.getOrderId());
        ProductReservationCancelledEvent productReservationCancelledEvent = new ProductReservationCancelledEvent(
                command.getProductId(),
                command.getOrderId()
        );
        kafkaTemplate.send(productEventsTopicName, productReservationCancelledEvent);
    }
}
