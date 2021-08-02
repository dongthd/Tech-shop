package com.java.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.java.domain.CartItem;
import com.java.domain.Product;
import com.java.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	private Map<Long, CartItem> map = new HashMap<Long, CartItem>();

	@Override
	public void add(CartItem item) {
		CartItem existedItem = map.get(item.getProductId());
		
		if (existedItem != null) {
			existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
//			existedItem.setUnitPrice(item.getUnitPrice() + (existedItem.getUnitPrice() * existedItem.getQuantity()));
			existedItem.setTotalPrice(item.getTotalPrice() + existedItem.getUnitPrice() * existedItem.getQuantity());
		} else {
			map.put(item.getProductId(), item);
		}
	}
//	double price = cartItem.getQuantity() * cartItem.getProduct().getUnitPrice();
//	totalPrice += price;
	
	@Override
	public void remove(CartItem item) {
		
		map.remove(item.getProductId());
		
	}
	
//	@Override
//	public void remove(CartItem item) {
//		CartItem existedItem = map.get(item.getProductId());
//		
//			map.remove(item);
//	}

	@Override
	public Collection<CartItem> getCartItems() {
		return map.values();
	}

	@Override
	public void clear() {
		map.clear();
	}
	
//	@Override
//	public void update(int productId, int quantity) {
//		CartItem item = map.get(productId);
//		
//		item.setQuantity(quantity);
//		
//		if (item.getQuantity() <= 0) {
//			map.remove(productId);
//		}
//	}
	
	@Override
	public double getAmount() {
	 return map.values().stream().mapToDouble(item->item.getQuantity()* item.getUnitPrice()).sum();
	}
	
	@Override
	public int getCount() {
		if (map.isEmpty()) {
			return 0;
		}
		
		return map.values().size();
	}

	

	@Override
	public void remove(Product product) {
		// TODO Auto-generated method stub
		
	}

}
