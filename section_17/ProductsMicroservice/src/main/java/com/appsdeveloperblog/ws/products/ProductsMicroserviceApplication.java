package com.appsdeveloperblog.ws.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.appsdeveloperblog.ws.products.service.ProductService;
import com.appsdeveloperblog.ws.products.service.ProductServiceImpl;

@SpringBootApplication
public class ProductsMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsMicroserviceApplication.class, args);
	}
	
}
