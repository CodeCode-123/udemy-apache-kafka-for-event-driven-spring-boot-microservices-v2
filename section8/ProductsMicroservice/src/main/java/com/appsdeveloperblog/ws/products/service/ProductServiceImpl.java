package com.appsdeveloperblog.ws.products.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
		
//		//send the event asynchronously
//		CompletableFuture<SendResult<String, Object>> future = 
//				kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
//		
//		future.whenComplete((result, exception) -> {
//			if (exception != null) {
//				LOGGER.error("********** Failed to send message: " + exception.getMessage());
//			} else {
//				LOGGER.info("********** Message sent successfully: " + result.getRecordMetadata());
//			}
//		});
		
		//send the event synchronously 
		LOGGER.info("Before publishing a ProductCreatedEvent");
		
		SendResult<String, Object> result = 
					kafkaTemplate.send("topic2", productId, productCreatedEvent).get();
		
		LOGGER.info("Partition: " + result.getRecordMetadata().partition());
		LOGGER.info("Topic: " + result.getRecordMetadata().topic());
		LOGGER.info("Offset: " + result.getRecordMetadata().offset());
		
		LOGGER.info("********** Returning product id");
		
		return productId;
	}

}
