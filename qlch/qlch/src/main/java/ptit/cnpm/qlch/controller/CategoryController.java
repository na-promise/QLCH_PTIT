/**
 * 
 */
package ptit.cnpm.qlch.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ptit.cnpm.qlch.entity.Category;
import ptit.cnpm.qlch.repository.CategoryRepo;

/**
 * @author ngoc-anh
 *
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
	private static Logger logger = LoggerFactory.getLogger(CategoryController.class);
	@Autowired
	CategoryRepo categoryRepo;

	@GetMapping("/create")
	public String create() {
		return "category/create";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute Category category) {
		categoryRepo.save(category);
		return "redirect:/category/search";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		categoryRepo.delete(categoryRepo.getById(id));
		return "redirect:/category/search";
	}

	@GetMapping("/update")
	public String update(Model model, @RequestParam("id") int id) {
		model.addAttribute("category", categoryRepo.getById(id));
		return "category/update";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Category category) {
		categoryRepo.save(category);
		return "redirect:/category/search";
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {

		if (page == null)
			page = 0;
		if (size == null)
			size = 5;

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		if (name != null && !name.isEmpty()) {
			Page<Category> pageableCategory = categoryRepo.search("%" + name + "%", pageable);
			model.addAttribute("list", pageableCategory.toList());
			model.addAttribute("totalPage", pageableCategory.getTotalPages());
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		} else if (id != null) {
			Category category = categoryRepo.findById(id).orElse(null);
			if (category != null) {
				model.addAttribute("list", Arrays.asList(category));
			} else {
				logger.info("");
			}
			model.addAttribute("totalPage", 1);
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		} else {
			Page<Category> pageableCategory = categoryRepo.findAll(pageable);
			model.addAttribute("totalPage", pageableCategory.getTotalPages());
			model.addAttribute("list", pageableCategory.toList());
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		}

		return "category/search";
	}
}
