package com.java.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.java.domain.Product;
import com.java.repository.CategoryRepository;
import com.java.repository.ProductRepository;
import com.java.service.CategoryService;
import com.java.service.ProductService;

@Controller
public class ShoppingController {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

 // note
	@GetMapping(value = "shop")
	public String shopping(Model model) {
//		int selectedPage = Integer.valueOf(page);
		model.addAttribute("categoryList", categoryService.listCategory());
		List<Product> productList = productService.listProduct();
		model.addAttribute("productList", productList);

		return "site/shopping";

	}
	
//	@GetMapping(value = "/home")
//	public String Home() {
//		return "redirect:/shop";
//	}
//	
//	@GetMapping(value = "shop")
//	public String shopping(Model model, HttpServletRequest request, RedirectAttributes redirect) {
////		int selectedPage = Integer.valueOf(page);
//		request.getSession().setAttribute("productList", null);
////		model.addAttribute("categoryList", categoryService.listCategory());
////		List<Product> productList = productService.listProduct();
////		model.addAttribute("productList", productList);
//
//		return "redirect:/shop/page/1";
//
//	}
//	
//	@GetMapping(value = "shop/page/{pageNumber}")
//	public String showShopPage(HttpServletRequest request, 
//			@PathVariable int pageNumber, Model model) {
//		PagedListHolder<?> pages = (PagedListHolder<?>) request.getSession().getAttribute("productList");
//		int pagesize = 3;
//		List<Product> list =(List<Product>) productService.findAll();
//		System.out.println(list.size());
//		if (pages == null) {
//			pages = new PagedListHolder<>(list);
//			pages.setPageSize(pagesize);
//		} else {
//			final int goToPage = pageNumber - 1;
//			if (goToPage <= pages.getPageCount() && goToPage >= 0) {
//				pages.setPage(goToPage);
//			}
//		}
//		request.getSession().setAttribute("productList", pages);
//		int current = pages.getPage() + 1;
//		int begin = Math.max(1, current - list.size());
//		int end = Math.min(begin + 5, pages.getPageCount());
//		int totalPageCount = pages.getPageCount();
//		String baseUrl = "/shop/page/";
//
//		model.addAttribute("beginIndex", begin);
//		model.addAttribute("endIndex", end);
//		model.addAttribute("currentIndex", current);
//		model.addAttribute("totalPageCount", totalPageCount);
//		model.addAttribute("baseUrl", baseUrl);
//		model.addAttribute("shops", pages);
//
//		return "site/shopping";
//	}
	
	
	
//	@GetMapping(value = "shop")
//	public String shopping(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") String page) {
//		int selectedPage = Integer.valueOf(page);
//		model.addAttribute("productList", getProductsPerPage(Integer.valueOf(selectedPage)));
//		model.addAttribute("startPage", selectedPage);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("visiblePages", totalPages > 3 ? (totalPages / 2) : totalPages);
////		List<Product> productList = productService.listProduct();
////		model.addAttribute("productList", productList);
//
//		return "site/shopping";
//
//	}
	
//	@GetMapping(value = "shop")
//    public String shop(Model model,
//                       @RequestParam(value = "page", required = false, defaultValue = "1") String page) {
//        int selectedPage = Integer.valueOf(page);
//        model.addAttribute("productList", getProductsPerPage(Integer.valueOf(selectedPage)));
//        model.addAttribute("startPage", selectedPage);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("visiblePages", totalPages > 3 ? (totalPages / 2) : totalPages);
//        return "shop";
//    }

	// show all product
	@GetMapping("allProduct")
	public String allProduct(Model model) {
		model.addAttribute("productList", productService.listProduct());
		model.addAttribute("categoryList", categoryService.listCategory());
		return "site/shopping";
	}

	// show product by category
	@GetMapping("getProducts/{categoryId}")
	public ModelAndView getProductFromCategory(@PathVariable("categoryId") String categoryId) {
		ModelAndView mv = new ModelAndView("site/shopping");
		long categoryLongId = Long.parseLong(categoryId);
		System.out.println(categoryLongId);
		mv.addObject("productList", productService.findByCategory(categoryLongId));
		mv.addObject("categoryList", categoryService.listCategory());
		return mv;
	}
	

}
