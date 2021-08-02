package com.java.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.domain.Customer;
import com.java.repository.CustomersRepository;

@Controller
@RequestMapping(value = "admin/customer")
public class CustomerController {
	
	@Autowired
	CustomersRepository customersRepository;
	
	@GetMapping(value = "")
	public String listCustomer(ModelMap model) {

		List<Customer> list = customersRepository.findAll();

		model.addAttribute("listCustomer", list);
		return "admin/customer";
	}
}
