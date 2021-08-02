package com.java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.domain.Customer;
import com.java.repository.CustomersRepository;

@Service
public class TechShopService implements UserDetailsService {

	@Autowired
	CustomersRepository customersRepository;

	@Override
	public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
//		Integer customerId = null;
		Optional<Customer> customer = Optional.ofNullable(customersRepository.findCustomersLogin(customerId));
		final Customer customerLogin = new Customer();
		customerLogin.setEnabled(customer.get().getEnabled());
		customerLogin.setCustomerId(customer.get().getCustomerId());
		customerLogin.setEmail(customer.get().getEmail());
		customerLogin.setPassword(customer.get().getPassword());
		customerLogin.setFullname(customer.get().getFullname());
		customerLogin.setRoleId(customer.get().getRoleId());
		return customerLogin;
	}

}
