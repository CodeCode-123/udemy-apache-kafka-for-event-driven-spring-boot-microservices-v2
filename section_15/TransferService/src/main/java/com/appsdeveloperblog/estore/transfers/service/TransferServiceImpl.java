package com.appsdeveloperblog.estore.transfers.service;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.appsdeveloperblog.estore.transfers.error.TransferServiceException;
import com.appsdeveloperblog.estore.transfers.events.DepositRequestedEvent;
import com.appsdeveloperblog.estore.transfers.events.WithdrawalRequestedEvent;
import com.appsdeveloperblog.estore.transfers.model.TransferRestModel;


@Service
public class TransferServiceImpl implements TransferService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private KafkaTemplate<String, Object> kafkaTemplate;
	private Environment environment;
	private RestTemplate restTemplate;

	public TransferServiceImpl(KafkaTemplate<String, Object> kafkaTemplate, Environment environment,
			RestTemplate restTemplate) {
		this.kafkaTemplate = kafkaTemplate;
		this.environment = environment;
		this.restTemplate = restTemplate;
	}

	//spring framework transaction, not Kafka specific, 
	//rollbackFor will not overwrite the rollback for other unchecked exceptions
	//noRollbackFor will not rollback for defined exceptions
//	@Transactional(value="kafkaTransactionManager", rollbackFor = {TransferServiceException.class}) 
	@Transactional
	@Override
	public boolean transfer(TransferRestModel transferRestModel) {
		WithdrawalRequestedEvent withdrawalEvent = new WithdrawalRequestedEvent(transferRestModel.getSenderId(),
				transferRestModel.getRecepientId(), transferRestModel.getAmount());
		DepositRequestedEvent depositEvent = new DepositRequestedEvent(transferRestModel.getSenderId(),
				transferRestModel.getRecepientId(), transferRestModel.getAmount());

		try {
			kafkaTemplate.send(environment.getProperty("withdraw-money-topic", "withdraw-money-topic"),
					withdrawalEvent);
			LOGGER.info("Sent event to withdrawal topic.");

			// Business logic that causes and error
			callRemoteServce();

			kafkaTemplate.send(environment.getProperty("deposit-money-topic", "deposit-money-topic"), depositEvent);
			LOGGER.info("Sent event to deposit topic");

		} catch (Exception ex) {
			LOGGER.error(ex.getMessage(), ex);
			throw new TransferServiceException(ex);
		}
		
//		//local transactions
//		boolean returnValue = kafkaTemplate.executeInTransaction(t -> {
//			t.send("withdraw-money-topic", withdrawalEvent);
//			LOGGER.info("Sent event to withdrawal topic.");
//
//			// Business logic that causes and error
//			try {
//				callRemoteServce();
//			} catch (Exception ex) {
//				LOGGER.error(ex.getMessage(), ex);
//				throw new TransferServiceException(ex);
//			}
//			
//			t.send("deposit-money-topic", depositEvent);
//			LOGGER.info("Sent event to deposit topic");
//			return true;
//		});		
		
		return true;
	}

	private ResponseEntity<String> callRemoteServce() throws Exception {
		String requestUrl = "http://localhost:8082/response/200";
		ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);

		if (response.getStatusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
			throw new Exception("Destination Microservice not availble");
		}

		if (response.getStatusCode().value() == HttpStatus.OK.value()) {
			LOGGER.info("Received response from mock service: " + response.getBody());
		}
		return response;
	}

}
