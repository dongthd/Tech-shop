package com.java.controller.admin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.java.domain.Category;
import com.java.model.CategoryDto;
import com.java.service.CategoryService;

@Controller
@RequestMapping(value = "/admin/category")
public class CategoryController {

//	@Autowired
//	CategoryRepository categoryRepository;

//	@RequestMapping("/category")
//	public String index(Model model) {
//		// tạo 1 đối tượng category
//		Category category = new Category();
//		model.addAttribute("category", category);
//
//		// lấy ra các danh mục
//		List<Category> listCategory = categoryRepository.findAll();
//		model.addAttribute("categories", listCategory);
//
//		return "admin/listCategory";
//	}
//
//	// add category
//	@RequestMapping(value = "/category/create")
//	public String create(@Valid Category category, Model model) {
//
//		categoryRepository.save(category);
//		
//		model.addAttribute("message", "Category is saved!");
//
//		return "redirect:/admin/category";
//	}
//
//	// edit category
//	@RequestMapping(value = "/category/edit/{categoryId}")
//	public String edit(Model model, @PathVariable("categoryId") Long categoryId) {
//		Category category = categoryRepository.findById(categoryId).get();
//		model.addAttribute("category", category);
//
//		List<Category> listCategory = categoryRepository.findAll();
//		model.addAttribute("categories", listCategory);
//
//		return "admin/listCategory";
//	}
//
//	// update category
//	@RequestMapping("/category/update")
//	public String update(Category category, Model model) {
//		categoryRepository.save(category);
//		model.addAttribute("message", "Category is saved!");
//
//		return "redirect:/category/edit/" + category.getCategoryId();
//	}
//
//	// delete category
//	@RequestMapping("/category/delete/{categoryId}")
//	public String delete(@PathVariable("categoryId") Long id) {
//		categoryRepository.deleteById(id);
//
//		return "redirect:/admin/category";
//	}

	// search

//	@GetMapping(value = "/category")
//	public String search(Model model,
//			@RequestParam(name = "name", required = false) String name,
//			@RequestParam("page") Optional<Integer> page,
//			@RequestParam("size") Optional<Integer> size) {
//		
////		Category category = new Category();
//		
//		
//		int currentPage = page.orElse(1);
//		int pageSize = size.orElse(5);
//		
//		Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by("name"));
//		Page<Category> resutlPage =null;
//		
//		
////		List<Category> list = null;
//		
//		
//		if (StringUtils.hasText(name)) {
//			resutlPage = categoryRepository.findByNameCategories(name, pageable);
//			model.addAttribute("name", name);
//		}else {
//			resutlPage =  categoryRepository.findAll(pageable);
//		}
//		
//		int totalPages = resutlPage.getTotalPages();
//		if (totalPages > 0) {
//			int start = Math.max(1, currentPage - 2);
//			int end = Math.min(currentPage + 2, totalPages);
//			
//			if (totalPages > 5) {
//				if (end == totalPages) start = end -5;
//				else if (start == 1) end = start + 5;
//			}
//			List<Integer> pageNumbers = IntStream.range(start, end)
//					.boxed()
//					.collect(Collectors.toList());
//			
//			model.addAttribute("pageNumbers",pageNumbers);
//		}
//			model.addAttribute("categoryPage", resutlPage);
//		
//	
//			return"admin/listCategory";
//		}
	@Autowired
	CategoryService categoryService;

	// show addCategory
	@GetMapping(value = "add")
	public String Add(Model model) {

		model.addAttribute("category", new CategoryDto());
		return "admin/addCategory";
	}

	// thêm mới category
	@PostMapping(value = "saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryDto dto,
			BindingResult result) {
		// check
		if (result.hasErrors()) {
			/* model.addAttribute("message","Thêm nhãn hiệu thất bại :("); */
			return new ModelAndView("admin/addCategory");
		}

		Category entity = new Category();
		BeanUtils.copyProperties(dto, entity);

		categoryService.save(entity);
		model.addAttribute("message", "Lưu nhãn hàng thành công <3");

		return new ModelAndView("forward:/admin/category", model);
	}

	@RequestMapping(value = "")
	public String listCategory(ModelMap model) {

		List<Category> list = categoryService.findAll();

		model.addAttribute("categories", list);
		return "admin/listCategory1";
	}

	@GetMapping(value = "edit/{categoryId}")
	public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		Optional<Category> opt = categoryService.findById(categoryId);
		CategoryDto dto = new CategoryDto();
		if (opt.isPresent()) {
			Category entity = opt.get();

			BeanUtils.copyProperties(entity, dto);

			dto.setIsEdit(true);

			model.addAttribute("category", dto);
			return new ModelAndView("admin/addCategory", model);
		}

		model.addAttribute("message", "Không tồn tại nhãn hiệu !");
		return new ModelAndView("redirect:/admin/category", model);
	}

	@GetMapping(value = "delete/{categoryId}")
	public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {

		categoryService.deleteById(categoryId);

		model.addAttribute("message", "Nhãn hiệu đã được xóa !");
		return new ModelAndView("forward:/admin/category/search", model);
	}

	@GetMapping(value = "search")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {

		List<Category> list = null;

		if (StringUtils.hasText(name)) {
			list = categoryService.findByNameContaining(name);

		} else {
			list = categoryService.findAll();
		}

		model.addAttribute("categories", list);

		return "admin/search";
	}

	@GetMapping(value = "searchpaginated")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

		int currentPage = page.orElse(1);
		int pageSize = size.orElse(5);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
		Page<Category> resutlPage = null;

		if (StringUtils.hasText(name)) {
			resutlPage = categoryService.findByNameContaining(name, pageable);
			model.addAttribute("name", name);

		} else {
			resutlPage = categoryService.findAll(pageable);
		}

		int totalPages = resutlPage.getTotalPages();
		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);

			if (totalPages > 5) {
				if (end == totalPages)
					start = end - 5;
				else if (start == 1)
					end = start + 5;
			}
			List<Integer> pageNumbers = IntStream.range(start, end).boxed().collect(Collectors.toList());

			model.addAttribute("pageNumbers", pageNumbers);

		}
		model.addAttribute("categoryPage", resutlPage);
		return "admin/searchpaginated";
	}

}
