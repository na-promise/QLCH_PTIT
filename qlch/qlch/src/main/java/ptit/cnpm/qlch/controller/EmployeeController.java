/**
 * 
 */
package ptit.cnpm.qlch.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import ptit.cnpm.qlch.entity.Employee;
import ptit.cnpm.qlch.repository.EmployeeRepo;

/**
 * @author ngoc-anh
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {
	private static Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	EmployeeRepo employeeRepo;

	@GetMapping("/create")
	public String create() {
		return "employee/create";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute Employee employee, 
			@RequestParam("date_birth") String dateOfBirth)
			throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		employee.setDateOfBirth(dateFormat.parse(dateOfBirth));
		employeeRepo.save(employee);
		return "redirect:/employee/search";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		employeeRepo.delete(employeeRepo.getById(id));
		return "redirect:/employee/search";
	}

	@GetMapping("/update")
	public String update(Model model, @RequestParam("id") int id) {
		model.addAttribute("employee", employeeRepo.getById(id));
		return "employee/update";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Employee employee,
			@RequestParam("date_birth") String dateOfBirth) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		employee.setDateOfBirth(dateFormat.parse(dateOfBirth));
		employeeRepo.save(employee);
		return "redirect:/employee/search";
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
			Page<Employee> pageableEmployee = employeeRepo.search("%" + name + "%", pageable);
			model.addAttribute("list", pageableEmployee.toList());
			model.addAttribute("totalPage", pageableEmployee.getTotalPages());
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		} else if (id != null) {
			Employee employee = employeeRepo.findById(id).orElse(null);
			if (employee != null) {
				model.addAttribute("list", Arrays.asList(employee));
			} else {
				logger.info("");
			}
			model.addAttribute("totalPage", 1);
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		} else {
			Page<Employee> pageableEmployee = employeeRepo.findAll(pageable);
			model.addAttribute("totalPage", pageableEmployee.getTotalPages());
			model.addAttribute("list", pageableEmployee.toList());
			model.addAttribute("page", page);
			model.addAttribute("size", size);
		}

		return "employee/search";
	}
}
