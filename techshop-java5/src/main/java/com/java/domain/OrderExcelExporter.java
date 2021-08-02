package com.java.domain;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.format.CellDateFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.Data;

@Data
public class OrderExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	private List<Order> listOrDetails;

	public OrderExcelExporter(List<Order> listOrDetails) {

		this.listOrDetails = listOrDetails;
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("OrderDetails");
	}

	private void writeHeaderRow() {

		Row row = sheet.createRow(0);

		Cell cell = row.createCell(0);
		cell.setCellValue("Mã Đơn Hàng");
		
		cell = row.createCell(1);
		cell.setCellValue("Thông Tin Sản Phẩm");

		cell = row.createCell(2);
		cell.setCellValue("Tổng Tiền");

		cell = row.createCell(3);
		cell.setCellValue("Tên Khách Hàng");

		cell = row.createCell(4);
		cell.setCellValue("Số Điện Thoại");

		cell = row.createCell(5);
		cell.setCellValue("Địa Chỉ");

//		cell = row.createCell(7);
//		cell.setCellValue("Ngày Đặt Hàng");

	}

	private void writeDataRows() {
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		int rowCount = 1;
		for (Order order : listOrDetails) {
			Row row = sheet.createRow(rowCount++);

			Cell cell = row.createCell(0);
			cell.setCellValue(order.getOrderId());

			cell = row.createCell(1);
			cell.setCellValue(order.getDescription());

			cell = row.createCell(2);
			cell.setCellValue(order.getTotalPrice());

			cell = row.createCell(3);
			cell.setCellValue(order.getReceiver());

			cell = row.createCell(4);
			cell.setCellValue(order.getPhone());
			
			cell = row.createCell(5);
			cell.setCellValue(order.getAddress());

		}

	}

	public void export(HttpServletResponse response) throws IOException {

		writeHeaderRow();
		writeDataRows();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();

	}

}
