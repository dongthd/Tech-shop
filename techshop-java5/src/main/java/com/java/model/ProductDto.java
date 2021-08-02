package com.java.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto implements Serializable{
	private Long productId;
	@NotEmpty
	@Min(value = 5)
	private String name;
	@Min(value = 0)
	private int quantity;
	@Min(value = 0)
	private double unitPrice;
	private String image;
	private MultipartFile imageFile;
	
	private String description;
	private double discount;
	private Date enteredDate;
	private short status;
	private Long categoryId;
	
	
	public boolean setIsEdit;

}
