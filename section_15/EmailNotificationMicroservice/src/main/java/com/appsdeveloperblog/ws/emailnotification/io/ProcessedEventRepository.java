package com.appsdeveloperblog.ws.emailnotification.io;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, Long> {
	
	Optional<ProcessedEventEntity> findByMessageId(String messageId);

}
