package com.java.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

	private Long productId;
	private String name;
	private int quantity;
	private double unitPrice;
	private double totalPrice;
	private Product product;

}
