package com.appsdeveloperblog.ws.products;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class IdempotentProducerIntegrationTest {

    @MockitoBean
    KafkaAdmin kafkaAdmin;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void testProducerConfig_whenIdempotenceEnabled_assertsIdempotentProperties() {
        //Arrange
        ProducerFactory<String, Object> producerFactory = kafkaTemplate.getProducerFactory();

        //Act
        Map<String, Object> config = producerFactory.getConfigurationProperties();

        //Assert
        assertTrue((Boolean) config.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG));
        assertTrue("all".equalsIgnoreCase((String) config.get(ProducerConfig.ACKS_CONFIG)));
        if (config.containsKey(ProducerConfig.RETRIES_CONFIG)) {
            assertTrue(Integer.parseInt(config.get(ProducerConfig.RETRIES_CONFIG).toString()) > 0);
        }

    }
}
