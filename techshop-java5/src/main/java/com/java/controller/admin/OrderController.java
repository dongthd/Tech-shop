package com.java.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.java.domain.Order;
import com.java.domain.OrderDetail;
import com.java.domain.OrderExcelExporter;
import com.java.repository.OrderDetailRepository;
import com.java.repository.OrderRepository;
import com.java.service.OrderDetailService;

@Controller
@RequestMapping(value = "/admin/order")
public class OrderController {
	
	@Autowired
	OrderDetailService orderDetailService;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	public OrderController(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
		this.orderRepository = orderRepository;
		this.orderDetailRepository = orderDetailRepository;
	}

	@ModelAttribute("orderList")
	public List<OrderDetail> showOrder(Model model) {
		List<OrderDetail> orderDetailList = (List<OrderDetail>) orderDetailRepository.findAll();
		return orderDetailList;
	}

	@RequestMapping(value = "")
	public String showproorders(Model model) {
		List<OrderDetail> orderDetails = (List<OrderDetail>) orderDetailRepository.lisorderbydesc();
		model.addAttribute("orderDetails", orderDetails);
		return "admin/order";
	}

	@GetMapping("editorder/{orderDetailId}")
	public String showEditOrder(@PathVariable("orderDetailId") int orderDetailId, Model model) {
		OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + orderDetailId));

		model.addAttribute("orderDetail", orderDetail);
		return "admin/checkorder";
	}

	// edit order
	@RequestMapping(value = "editorder", method = RequestMethod.POST)
	public String editordertr(@ModelAttribute("orderDetail") OrderDetail orderDetail, Model model,
			RedirectAttributes rs) {
		OrderDetail orderDetail2 = orderDetailRepository.save(orderDetail);
		if (null != orderDetail2) {
			model.addAttribute("message", "Cập nhật thành công !");
			model.addAttribute("orderDetail", orderDetailRepository.findById(orderDetail2.getOrderDetailId()));
		} else {
			model.addAttribute("message", "Cập nhất thất bại !");
			model.addAttribute("orderDetail", orderDetail);
		}
		return "admin/checkorder";
	}

	// delete oder
	@GetMapping("/delete/{orderDetailId}")
	public String deleteOrderDetail(@PathVariable("orderDetailId") int orderDetailId, Model model) {
		OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + orderDetailId));
		orderDetailRepository.delete(orderDetail);
		if (null != orderDetail) {
			model.addAttribute("message", "Hủy đơn hàng thành công !");
			model.addAttribute("orderDetail", orderDetailRepository.findById(orderDetail.getOrderDetailId()));
		} else {
			model.addAttribute("message", "Hủy thất bại !");
			model.addAttribute("orderDetail", orderDetail);
		}
		return "forward:/admin/order";
	}
	
	
	@GetMapping(value = "CustomerOrder")
	public String listCustomer(ModelMap model) {

		List<Order> list = orderRepository.findAll();

		model.addAttribute("listOrder", list);
		
		return "admin/listCustomer";
	}
	
	
	// to excel
	@GetMapping(value = "/export")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=orders.xlsx";
		
		response.setHeader(headerKey, headerValue);
		
		List<Order> lisOrders = orderDetailService.listAll();
		
		OrderExcelExporter excelExporter = new OrderExcelExporter(lisOrders);
		excelExporter.export(response);
		
	}

	

}
