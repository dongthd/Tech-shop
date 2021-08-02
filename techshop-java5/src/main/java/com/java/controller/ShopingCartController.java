package com.java.controller;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.domain.CartItem;
import com.java.domain.Order;
import com.java.domain.OrderDetail;
import com.java.domain.Product;
import com.java.repository.OrderDetailRepository;
import com.java.repository.OrderRepository;
import com.java.repository.ProductRepository;
import com.java.service.ProductService;
import com.java.service.ShoppingCartService;

@Controller
//@RequestMapping(value = "shoppingCart")
public class ShopingCartController {

	@Autowired
	ProductService productService;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Autowired
	ShoppingCartService shoppingCartService;

	
	@GetMapping(value = "cartItem")
	public String shoppingCart(Model model) {
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getUnitPrice();
			totalPrice += price;
		}

		model.addAttribute("totalPrice", totalPrice);
		return "site/shoppingCart";
	}

	// add cartItem
//	@GetMapping(value = "add")
//	public String add(@RequestParam("productId") Integer productId) {
//		Product product = productService.findById(Long.valueOf(productId)).get();
//		if (product != null) {
//			CartItem item = new CartItem();
//			BeanUtils.copyProperties(product, item);
//			item.setQuantity(1);
//			item.setProduct(product);
//			shoppingCartService.add(item);
//		}
//		return "redirect:/cartItem";
//	}

	// add cartItem
	@GetMapping(value = "add")
	public String add(@RequestParam("productId") Integer productId, HttpServletRequest request) {
		Product product = productService.findById(Long.valueOf(productId)).get();

		HttpSession session = request.getSession();
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setQuantity(1);
			item.setProduct(product);
			shoppingCartService.add(item);
		}

//		double totalPrice = 10.0;
//		for (CartItem item : cartItems) {
//			double price = item.getQuantity() * item.getProduct().getUnitPrice();
//			totalPrice += price;
//		}

//		session.setAttribute("totalPrice", totalPrice);
		session.setAttribute("cartItems", cartItems);
		return "redirect:/cartItem";
	}

//	@GetMapping(value = "shoppingCart")
//	public String viewCart(Model model, HttpServletRequest request) {
//		HttpSession httpSession = request.getSession();
//		Object s = httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
//		return "shoppingCart";
//	}

	// delete cartItem
	@GetMapping(value = "/remove")
	public String remove(@RequestParam("productId") Long productId, HttpServletRequest request) {
		Product product = productService.findById(Long.valueOf(productId)).get();

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		HttpSession session = request.getSession();
		if (product != null) {
			CartItem item = new CartItem();
			BeanUtils.copyProperties(product, item);
			item.setProduct(product);
			cartItems.remove(session);
			shoppingCartService.remove(item);
		}

		return "redirect:/cartItem";
	}

	
	// show checkout
	@GetMapping(value = "checkout")
	public String checkout(Model model) {

		Order order = new Order();
		model.addAttribute("order", order);

		Collection<CartItem> cartItems = shoppingCartService.getCartItems();
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("total", shoppingCartService.getAmount());
		model.addAttribute("NoOfItems", shoppingCartService.getCount());
		double totalPrice = 0;
		for (CartItem cartItem : cartItems) {
			double price = cartItem.getQuantity() * cartItem.getProduct().getUnitPrice();
			totalPrice += price;
		}

		model.addAttribute("totalPrice", totalPrice);

		return "site/checkout";
	}

	// submit checkout
	@PostMapping(value = "/checkout")
	@Transactional
	public String CheckedOut(Model model, Order order, HttpServletRequest request) {

		HttpSession session = request.getSession();

		orderRepository.save(order);

		// List<CartItem> cartItems = (List<CartItem>)
		// session.getAttribute("cartItems");
		Collection<CartItem> cartItems = shoppingCartService.getCartItems();

		double totalPrice = 0;

		for (CartItem cartItem : cartItems) {

			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setQuantity(cartItem.getQuantity());
			orderDetail.setOrder(order);
			orderDetail.setProduct(cartItem.getProduct());

			double price = cartItem.getQuantity() * cartItem.getProduct().getUnitPrice();
			totalPrice += price;
			orderDetail.setTotalPrice(price);
			orderDetail.setStatus("Đang chờ xử lý !!!");
			orderDetailRepository.save(orderDetail);
		}

		order.setTotalPrice(totalPrice);
		Date date = new Date();
		order.setOrderDate(date);
		orderRepository.save(order);
		order.getOrderId();
		System.out.println(order.getOrderId());

		session.removeAttribute("cartItems");
		model.addAttribute("orderId", order.getOrderId());

		return "site/checkout_success";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}
