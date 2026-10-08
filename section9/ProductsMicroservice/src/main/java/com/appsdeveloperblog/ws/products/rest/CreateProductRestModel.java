package com.appsdeveloperblog.ws.products.rest;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRestModel {
	private String title;
	private BigDecimal price;
	private Integer quantity;
	
	@Override
	public String toString() {
		return "CreateProductRestModel [title=" + title + ", price=" + price + ", quantity=" + quantity + "]";
	}
}
