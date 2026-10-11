package com.appsdeveloperblog.ws.products;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.condition.EmbeddedKafkaCondition;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import com.appsdeveloperblog.ws.products.rest.CreateProductRestModel;
import com.appsdeveloperblog.ws.products.service.ProductCreatedEvent;
import com.appsdeveloperblog.ws.products.service.ProductService;
import com.appsdeveloperblog.ws.products.service.ProductServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EmbeddedKafka(topics = ProductsServiceIntegrationTest3.TEMPLATE_TOPIC)
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}", classes=KafkaConfig.class)
public class ProductsServiceIntegrationTest3 {
	
	@Autowired
	private ProductServiceImpl productService;
	
	public static final String TEMPLATE_TOPIC = "templateTopic";
    public static EmbeddedKafkaBroker embeddedKafka;
    
    private static KafkaMessageListenerContainer<String, Object> container;
	private static BlockingQueue<ConsumerRecord<String, Object>> records;
	
	public ProductsServiceIntegrationTest3() {
		MockitoAnnotations.openMocks(this);
	}

    @BeforeAll
	public static void setUp() {
		embeddedKafka = EmbeddedKafkaCondition.getBroker();
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(embeddedKafka, "testT", false);
        DefaultKafkaConsumerFactory<String, Object> cf =
                            new DefaultKafkaConsumerFactory<>(consumerProps);
        ContainerProperties containerProperties = new ContainerProperties(TEMPLATE_TOPIC);
        container = new KafkaMessageListenerContainer<>(cf, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener(new MessageListener<String, Object>() {

            @Override
            public void onMessage(ConsumerRecord<String, Object> record) {
                System.out.println(record);
                records.add(record);
            }

        });
        container.setBeanName("templateTests");
        container.start();
        ContainerTestUtils.waitForAssignment(container,
                            embeddedKafka.getPartitionsPerTopic());
	}
    
    @Test
	void testCreatedProduct_whenGivenValidProductDetails_successfullySendsKafkaMessage() throws Exception {
		// Arrange
		String title = "iPhone 11";
		BigDecimal price = new BigDecimal(600);
		Integer quantity = 1;
		
		CreateProductRestModel createProductRestModel = new CreateProductRestModel();
		createProductRestModel.setPrice(price);
		createProductRestModel.setQuantity(quantity);
		createProductRestModel.setTitle(title);
		
		// Act
		productService.createProduct(createProductRestModel);
		
		// Assert
		ConsumerRecord<String, Object> message = records.poll(3000, TimeUnit.MILLISECONDS);
		assertNotNull(message);
		assertNotNull(message.key());
		ProductCreatedEvent productCreatedEvent = (ProductCreatedEvent) message.value();
		assertEquals(createProductRestModel.getQuantity(), productCreatedEvent.getQuantity());
		assertEquals(createProductRestModel.getTitle(), productCreatedEvent.getTitle());
		assertEquals(createProductRestModel.getPrice(), productCreatedEvent.getPrice());
	}
	

//    @Test
//    public void testTemplate() throws Exception {
//        
//        Map<String, Object> producerProps =
//                            KafkaTestUtils.producerProps(embeddedKafka);
//        ProducerFactory<String, Object> pf =
//                            new DefaultKafkaProducerFactory<>(producerProps);
//        KafkaTemplate<String, Object> template = new KafkaTemplate<>(pf);
//        template.setDefaultTopic(TEMPLATE_TOPIC);
//        template.sendDefault("foo");
//        assertEquals(records.poll(10, TimeUnit.SECONDS).value(), "foo");
//        template.sendDefault(0, "2", "bar");
//        ConsumerRecord<String, Object> received = records.poll(10, TimeUnit.SECONDS);
//        assertEquals(received.key(), "2");
//        assertEquals(received.partition(), 0);
//        assertEquals(received.value(), "bar");
//        template.send(TEMPLATE_TOPIC, 0, "2", "baz");
//        received = records.poll(10, TimeUnit.SECONDS);
//        assertEquals(received.key(), "2");
//        assertEquals(received.partition(), 0);
//        assertEquals(received.value(), "baz");
//    }

}
