package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.java.domain.OrderDetail;

@Repository
public interface OrderDetailRepository extends CrudRepository<OrderDetail, Integer> {

	@Query(value = "SELECT * FROM orderdetails limit 2", nativeQuery = true)
	public List<OrderDetail> toporder();

	/// list order theo sp tăng > giảm
	@Query(value = "SELECT * from orderdetails ORDER BY orderDetailId desc\n ", nativeQuery = true)
	public List<OrderDetail> lisorderbydesc();
	
    /// thống kê theo san pham
    @Query(value = "SELECT p.name ,  \n" +
            "SUM(o.quantity) as quantity ,    \n" +
            "SUM(o.quantity * o.total_price) as sum, \n" +
            "AVG(o.total_price) as avg  , \n" +
            "Min(o.total_price) as min  , \n" +
            "max(o.total_price) as max \n" +
            "FROM orderdetails o\n" +
            "INNER JOIN products p ON o.productId = p.productId\n" +
            "GROUP BY p.name;", nativeQuery = true)

    public List<Object[]> repo();
    
    
 // thong ke san pham theo tháng // theo các Tháng
    @Query(value = "Select month(od.orderDate) , \n" +
            "SUM(o.quantity) as quantity ,    \n" +
            "SUM(o.quantity * o.total_price) as sum, \n" +
            "AVG(o.total_price) as avg  , \n" +
            "Min(o.total_price) as min  , \n" +
            "max(o.total_price) as max \n" +
            "FROM orderdetails o\n" +
            "INNER JOIN orders od ON o.orderId =od.orderId\n" +
            "GROUP BY month(od.orderDate);", nativeQuery = true)

    public List<Object[]> repowheremonth();

}
