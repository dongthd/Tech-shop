package com.java.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.java.domain.Order;
import com.java.repository.OrderRepository;

@Controller
public class listCustomerOrderController {
	
	@Autowired
	OrderRepository orderRepository;

	@GetMapping(value = "listCustomerOrder")
	public String listCustomer(ModelMap model) {

		List<Order> list = orderRepository.findAll();

		model.addAttribute("listOrder", list);
		return "admin/listCustomer";
	}
}
