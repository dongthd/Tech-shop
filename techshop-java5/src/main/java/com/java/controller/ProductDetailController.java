package com.java.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.domain.Product;
import com.java.repository.ProductRepository;


@Controller
public class ProductDetailController {

	@Autowired
	ProductRepository productRepository;
	
	public ProductDetailController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
	
	@GetMapping(value = "productDetail")
	public String name() {
		 return "site/productDetail";
	}
	
//	@ModelAttribute(value = "customer")
//    public Customer initCustomer(Principal principal) {
//        Customer customer = new Customer();
//        if (principal != null) {
//            customer = (Customer) ((Authentication) principal).getPrincipal();
//        }
//        return customer;
//    }
//
//    @ModelAttribute("categoryList")
//    public List<Category> showCategory(Model model) {
//        List<Category> categoryList =
//                (List<Category>) categoryRepository.findAll();
//        return categoryList;
//    }

//	@GetMapping(value = "productDetail")
//	public String productDetail(@RequestParam("productId")
//					String productId, ModelMap modelMap) {
//		Optional<Product> product = productRepository.findById(Long.valueOf(productId));
//
//		List<Product> listProductByCategory = productRepository
//				.findByCategoryId(product.get().getCategory().getCategoryId());
//
//		if (product.isPresent()) {
//			modelMap.addAttribute("product", product.get());
//		}
//
//		if (listProductByCategory != null) {
//			modelMap.addAttribute("listProductByCategory", listProductByCategory);
//		}
//
//		return "site/productDetail";
//	}
}
