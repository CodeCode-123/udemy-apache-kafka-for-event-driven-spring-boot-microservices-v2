package com.appsdeveloperblog.ws.products.rest;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {
	private Date timestamp;
	private String message;
	private String details;
	
	@Override
	public String toString() {
		return "ErrorMessage [timestamp=" + timestamp + ", message=" + message + ", details=" + details + "]";
	}
}
