package com.java.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.domain.Category;
import com.java.domain.Product;
import com.java.repository.CategoryRepository;
import com.java.repository.OrderDetailRepository;
import com.java.repository.ProductRepository;

@Controller
public class IndexController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

//	public IndexController(CategoryRepository categoryRepository, ProductRepository productRepository,
//			OrderDetailRepository orderDetailRepository) {
//		this.productRepository = productRepository;
//		this.orderDetailRepository = orderDetailRepository;
//		this.categoryRepository = categoryRepository;
//	}

	@ModelAttribute("categoryList")
	public List<Category> showCategory(Model model) {

		List<Category> categoryList = (List<Category>) categoryRepository.findAll();

		return categoryList;
	}

	// hien thi trang index
	@GetMapping(value = "/")
	public String index(Model model) {

		Product product = new Product();

		model.addAttribute("product", product);

		return "site/index";

	}

	// list product ở trang chủ
	@ModelAttribute("listproduct6")
	public List<Product> listproduct6(Model model) {

		List<Product> productList = (List<Product>) productRepository.listproduct8();

		return productList;
	}

	// hiển thị miêu tả sản phẩm
	@RequestMapping(value = "detail")

	public String showProductDetail(@RequestParam("productId") String productId, Model model) {

		Optional<Product> product = productRepository.findById(Long.valueOf(productId));

		model.addAttribute("product", product);
		return "detail";
	}

	// show check out page
	@RequestMapping(value = "/searchProduct")
	public String showseachr(Model model, @RequestParam("name") String name) {
		List<Product> productList = (List<Product>) productRepository.searchProduct(name);
		model.addAttribute("productList", productList);
		return "site/shopping";
	}
}
