package com.java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.java.domain.Order;
import com.java.repository.OrderRepository;

@Service
public class OrderDetailService {
	
	@Autowired
	OrderRepository repo;
	
	public List<Order> listAll(){
		
		return (List<Order>) repo.findAll();
	}

}
