package com.java.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.java.domain.Customer;
import com.java.domain.Role;
import com.java.repository.CustomersRepository;

@Controller
public class RegisterController {

	@Autowired
	CustomersRepository customersRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping(value = "register")
	public String register() {

		return "site/register";

	}

	// customer
	@ModelAttribute(value = "customer")
	public Customer initCustomer(Principal principal) {
		Customer customer = new Customer();
		if (principal != null) {
			customer = (Customer) ((Authentication) principal).getPrincipal();
		}
		return customer;
	}

	// register
	@RequestMapping(value = "/registered")
	public String addCourse(@ModelAttribute("customer") Customer customer, ModelMap model) {

		customer.setEnabled(true);
		customer.setRoleId("0");
		customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));

		Customer c = customersRepository.save(customer);
		Role role = new Role();
		role.setRole("ROLE_USER");
		role.setCustomer(customer);
		if (null != c) {
			model.addAttribute("message", "Thành công");
			model.addAttribute("customer", customer);
		} else {
			model.addAttribute("message", "Thất bại");
			model.addAttribute("customer", customer);
		}
		return "redirect:/login?register-success";
//		return "redirect:/register?register-success";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}
