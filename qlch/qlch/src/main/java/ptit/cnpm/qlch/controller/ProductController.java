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

import ptit.cnpm.qlch.entity.Product;
import ptit.cnpm.qlch.repository.CategoryRepo;
import ptit.cnpm.qlch.repository.ProductRepo;

/**
 * @author ngoc-anh
 *
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	private static Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	ProductRepo productRepo;

	@Autowired
	CategoryRepo categoryRepo;

	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("category", categoryRepo.findAll());
		return "product/create";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute Product product, @RequestParam("category") int id) {
		product.setCategory(categoryRepo.getById(id));
		productRepo.save(product);
		return "redirect:/product/search";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		productRepo.delete(productRepo.getById(id));
		return "redirect:/product/search";
	}

	@GetMapping("update")
	public String update(Model model, @RequestParam("id") int id) {
		model.addAttribute("product", productRepo.getById(id));
		return "product/update";
	}

	@PostMapping("update")
	public String update(@ModelAttribute Product product) {
		return "";
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {

		if (page == null)
			page = 0;
		if (size == null)
			size = 5;
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

		if (name != null && !name.isEmpty()) {
			Page<Product> pageableProduct = productRepo.search("%" + name + "%", pageable);
			model.addAttribute("list", pageableProduct.toList());
			model.addAttribute("totalPage", pageableProduct.getTotalPages());
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		} else if (id != null) {
			Product product = productRepo.findById(id).orElse(null);
			if (product != null) {
				model.addAttribute("list", Arrays.asList(product));
			} else {
				logger.info("");
			}
			model.addAttribute("totalPage", 1);
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		} else {
			Page<Product> pageableProduct = productRepo.findAll(pageable);
			model.addAttribute("list", pageableProduct.toList());
			model.addAttribute("totalPage", pageableProduct.getTotalPages());
			model.addAttribute("size", size);
			model.addAttribute("page", page);
		}
		return "product/search";
	}
}
