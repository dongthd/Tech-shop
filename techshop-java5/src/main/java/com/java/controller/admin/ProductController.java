package com.java.controller.admin;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.java.domain.Category;
import com.java.domain.Product;
import com.java.model.CategoryDto;
import com.java.model.ProductDto;
import com.java.repository.ProductRepository;
import com.java.service.CategoryService;
import com.java.service.ProductService;
import com.java.service.impl.FileSystemStorageServiceImpl;

@Controller
@RequestMapping(value = "/admin/product")
public class ProductController {

	@Value("${storage.location}")
	private String pathUploadImage;

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRepository productRepository;

	@Autowired
	FileSystemStorageServiceImpl storageService;

	// show list category trong product
	@ModelAttribute("categoryList")
	public List<CategoryDto> getCategories() {
		return categoryService.findAll().stream().map(item -> {
			CategoryDto dto = new CategoryDto();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).collect(Collectors.toList());
	}

	// show add
	@GetMapping(value = "add")
	public String add(Model model) {
		ProductDto dto = new ProductDto();
		dto.setSetIsEdit(false);
		model.addAttribute("product", dto);

		return "admin/addProduct";
	}

	// edit product
	@GetMapping(value = "edit/{productId}")
	public ModelAndView edit(ModelMap model, @PathVariable("productId") Long productId) {

		Optional<Product> opt = productService.findById(productId);
		ProductDto dto = new ProductDto();

		if (opt.isPresent()) {
			Product entity = opt.get();

			BeanUtils.copyProperties(entity, dto);

			dto.setCategoryId(entity.getCategory().getCategoryId());
			dto.setIsEdit = true;

			model.addAttribute("product", dto);

			return new ModelAndView("admin/addProduct", model);

		}

		model.addAttribute("message", "Product is not existed !");

		return new ModelAndView("forward:/admin/product", model);
	}

	@GetMapping(value = "/images/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.laodAsResource(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	// delete product
	@GetMapping(value = "delete/{productId}")
	public ModelAndView delete(ModelMap model, @PathVariable("productId") Long productId) throws IOException {

		Optional<Product> opt = productService.findById(productId);

		if (opt.isPresent()) {

			if (!StringUtils.isEmpty(opt.get().getImage())) {

				storageService.delete(opt.get().getImage());

			}

			productService.delete(opt.get());
			model.addAttribute("message", "Sản phẩn đã được xóa !");

		} else {

			model.addAttribute("message", "Xóa sản phẩm thất bại !");
		}

		return new ModelAndView("forward:/admin/product", model);
	}

//	// create product
	@RequestMapping(value = "saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("product") ProductDto dto,
			BindingResult result, @RequestParam("imageFile") MultipartFile file) {

//		if (result.hasErrors()) {
//			model.addAttribute("message", "Thêm sản phẩm thất bại :(");
//			return new ModelAndView("admin/addProduct");
//		}
		storageService.store(file, file.getOriginalFilename());
		dto.setImage(file.getOriginalFilename());
		
		Product entity = new Product();
		BeanUtils.copyProperties(dto, entity);

		Category category = new Category();
		category.setCategoryId(dto.getCategoryId());
		entity.setCategory(category);

		if (dto.getImageFile().isEmpty()) {
			UUID uuid = UUID.randomUUID();
			String uuString = uuid.toString();

			entity.setImage(storageService.getStorageFilename(dto.getImageFile(), uuString));
			storageService.store(dto.getImageFile(), entity.getImage());
		}

		productService.save(entity);

		model.addAttribute("message", "Lưu sản phẩm thành công <3");

		return new ModelAndView("forward:/admin/product", model);
	}

//	// thêm product
//	@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
//	public String addProduct(@ModelAttribute("product") Product product, ModelMap model,
//			@RequestParam("imageFile") MultipartFile file, HttpServletRequest httpServletRequest) {
//
//		storageService.store(file, file.getOriginalFilename());
//
//		product.setImage(file.getOriginalFilename());
//		Product p = productService.save(product);
//		if (null != p) {
//			model.addAttribute("message", "Update success");
//			model.addAttribute("product", product);
//		} else {
//			model.addAttribute("message", "Update failure");
//			model.addAttribute("product", product);
//		}
//		return "redirect:/admin/product";
//	}

	// show list products
	@RequestMapping(value = "")
	public String list(ModelMap model) {
		List<Product> list = productService.findAll();

		model.addAttribute("products", list);

		return "admin/listProduct";
	}
	
	@GetMapping(value = "searchProduct")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {

		List<Product> list = null;

		if (StringUtils.hasText(name)) {
			list = productService.findByNameContaining(name);

		} else {
			list = productService.findAll();
		}

		model.addAttribute("products", list);

		return "admin/searchProduct";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

}
