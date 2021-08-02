package com.java.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.domain.Order;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Integer> {

	// danh sách sản phẩm đã order bởi custommer ID
	@Query(value = "select *from orders where customerId = ?", nativeQuery = true)
	public List<Order> listoderbycus(String customerId);

}
