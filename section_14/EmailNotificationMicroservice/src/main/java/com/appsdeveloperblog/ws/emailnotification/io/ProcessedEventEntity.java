package com.appsdeveloperblog.ws.emailnotification.io;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="processed_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedEventEntity implements Serializable {

	private static final long serialVersionUID = 3687553269742697084L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="message_id", nullable=false, unique=true)
	private String messageId;
	
	@Column(name="product_id", nullable=false)
	private String productId;

	public ProcessedEventEntity(String messageId, String productId) {
		this.messageId = messageId;
		this.productId = productId;
	}
	
}
