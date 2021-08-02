package com.java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.domain.Customer;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, String> {

	@Query(value = "select *from customers where customerId = ?", nativeQuery = true)
	public Customer findCustomersLogin(String customerId);
}
