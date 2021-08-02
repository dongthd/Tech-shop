package com.java.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByNameContaining(String name);

	Page<Category> findByNameContaining(String name, Pageable pageable);

	// Tìm kiểm sản phẩm
	@Query(value = "SELECT  c.category_name ,\n" + "  COUNT(*) AS count\n" + "FROM products p\n"
			+ "JOIN categories c ON p.categoryId = c.categoryId\n" + "GROUP BY c.category_name", nativeQuery = true)

	public List<Object[]> listAndCountCategory();
}
