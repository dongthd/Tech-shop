package com.java.service;


import java.util.Collection;

import org.springframework.stereotype.Service;

import com.java.domain.CartItem;
import com.java.domain.Product;


@Service
public interface ShoppingCartService {
	
	int getCount();

	double getAmount();

//	void update(int productId, int quantity);

	void clear();
	
	Collection<CartItem> getCartItems();

	void remove(CartItem item);

	void add(CartItem item);

	void remove(Product product);
	
//	void remove(CartItem item);

}
