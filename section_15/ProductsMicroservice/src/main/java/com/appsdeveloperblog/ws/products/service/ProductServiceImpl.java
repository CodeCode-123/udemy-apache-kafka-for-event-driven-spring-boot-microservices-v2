package com.appsdeveloperblog.ws.products.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;


@Service
public class ProductServiceImpl implements ProductService {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public ProductServiceImpl(KafkaTemplate<String, Object> kafkaTemplate) {
		super();
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public String createProduct(CreateProductRestModel productRestModel) throws Exception {
		String productId = Uuid.randomUuid().toString();
		
		// TODO: Persist Product Details into database table before publishing an Event
		
		ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
				productRestModel.getTitle(), productRestModel.getPrice(),
				productRestModel.getQuantity());
		
		//Created a ProducerRecord to added a parameter in the headers
		ProducerRecord<String, Object> record = new ProducerRecord<>(
				"product-created-events-topic",
				productId,
				productCreatedEvent);
		
		//Added messageId in the headers for idempotent check
		record.headers().add("messageId", Uuid.randomUuid().toString().getBytes());
		
		CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(record);
		
		future.whenComplete((result, exception) -> {
			if (exception != null) {
				LOGGER.error("********** Failed to send message: " + exception.getMessage());
			} else {
				LOGGER.info("********** Message sent successfully: " + result.getRecordMetadata());
				LOGGER.info("Partition: " + result.getRecordMetadata().partition());
				LOGGER.info("Topic: " + result.getRecordMetadata().topic());
				LOGGER.info("Offset: " + result.getRecordMetadata().offset());
			}
		});
		
		LOGGER.info("********** Returning product id");
		
		return productId;
	}

}
