package com.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	
	
	public List<Product> findByCategory_CategoryId(long CategoryId);
	
	//List Sản phẩm by danh mục
    @Query(value = "select  *from products where categoryId = ? ORDER BY enteredDate DESC limit 4", nativeQuery = true)
    public List<Product> findByCategoryId (Long categoryId);

	@Query(value = " select  *from products where productId = ?", nativeQuery = true)
	public Product findByIdProduct(Long productId);

	// danh sach san pham theo danh muc
	@Query(value = "select  *from products where categoryId = ? ", nativeQuery = true)
	public List<Product> listproductBycategory(Long categoryId);
	
	//List Sản phẩm by danh mục  /// Productdetails
    @Query(value = "select  *from products where categoryId = ? ORDER BY productDate DESC limit 4", nativeQuery = true)
    public List<Product> findByCategoryId (int categoryId);

	// hien thi cac san pham moi nhat o trang chu
	@Query(value = " SELECT * FROM products ORDER BY enteredDate DESC limit 8", nativeQuery = true)
	public List<Product> listproduct8();

	// Tìm kiểm sản phẩm theo tên sp
	@Query(value = "select *from products where name LIKE %:name%", nativeQuery = true)
	public List<Product> searchProduct(String name);
	
	List<Product> findByNameContainingOrCategoryNameContaining(String productName, String categoryName);

	
	//lọc giá cả từ 50000>120000
    @Query(value = "SELECT * FROM products WHERE unitPrice >= 50000 AND unitPrice <= 100000; " , nativeQuery = true)
    public List<Product> filterprice();
    
  //lọc giá cả từ 100000>150000
    @Query(value = "SELECT * FROM products WHERE unitPrice >= 100000 AND unitPrice <= 150000; " , nativeQuery = true)
    public List<Product> filterprice1015();
    
  //lọc giá cả từ 150000>300000
    @Query(value = "SELECT * FROM products WHERE unitPrice >= 150000 AND unitPrice <= 300000; " , nativeQuery = true)
    public List<Product> filterprice1530();
    
  //lọc giá cả từ 300000 > 1trieu
    @Query(value = "SELECT * FROM products WHERE unitPrice >= 300000 AND unitPrice <= 1000000; " , nativeQuery = true)
    public List<Product> filterprice301000();

	public List<Product> findByNameContaining(String name);
}
	