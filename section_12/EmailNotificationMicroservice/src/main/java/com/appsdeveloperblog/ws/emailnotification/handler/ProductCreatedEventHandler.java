package com.appsdeveloperblog.ws.emailnotification.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.appsdeveloperblog.ws.emailnotification.error.NotRetryableException;
import com.appsdeveloperblog.ws.emailnotification.error.RetryableException;
import com.appsdeveloperblog.ws.emailnotification.event.ProductCreatedEvent;

@Component
@KafkaListener(topics="product-created-events-topic")
public class ProductCreatedEventHandler {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final RestTemplate restTemplate;
	
	@Value("${app.request.url}")
	private String requestUrl;
	
	public ProductCreatedEventHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@KafkaHandler
	public void handler(ProductCreatedEvent productCreatedEvent) {
		//test NotRetryableException
//		if (true) {
//			throw new NotRetryableException("An error took place. No need to consume this message again.");
//		}
		LOGGER.info("Received a new event: {}", productCreatedEvent.getTitle());
		
		//test RetryableException
		try {
			ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
			
			if (response.getStatusCode().value() == HttpStatus.OK.value()) {
				LOGGER.info("Received response from a remote service: {}", response.getBody());
			}
		} catch (ResourceAccessException ex) {
			LOGGER.error(ex.getMessage());
			throw new RetryableException(ex);
		} catch (HttpServerErrorException ex) {
			LOGGER.error(ex.getMessage());
			throw new NotRetryableException(ex);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new NotRetryableException(ex);
		}
	}

}
