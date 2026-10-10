package com.appsdeveloperblog.estore.transfers.io;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(nullable=false)
	private String transferId;
	
	@Column(nullable=false)
	private String senderId;
	
	@Column(nullable=false)
	private String recepientId;
	
	@Column(nullable=false)
	private BigDecimal amount;
	
}
