package com.java.controller.admin;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.java.domain.OrderDetail;
import com.java.repository.OrderDetailRepository;

@Controller
@RequestMapping(value = "/admin/report")
public class ReportController {

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@GetMapping(value = "")
	public String report(Model model) throws SQLException {
		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listTest = orderDetailRepository.repo();
		System.out.println("id = " + listTest.get(0)[0] + "productId = " + listTest.get(0)[1]);
		model.addAttribute("listTest", listTest);
		return "admin/report";
	}

	// thống kê theo tháng
	@RequestMapping(value = "reportmonth")
	public String reportmonth(Model model) throws SQLException {
		OrderDetail orderDetail = new OrderDetail();
		model.addAttribute("orderDetail", orderDetail);
		List<Object[]> listTest = orderDetailRepository.repowheremonth();
		System.out.println("id = " + listTest.get(0)[0] + "productId = " + listTest.get(0)[1]);
		model.addAttribute("listTest", listTest);
		return "admin/report";
	}
}
