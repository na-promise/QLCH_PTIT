/**
 * 
 */
package ptit.cnpm.qlch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptit.cnpm.qlch.entity.Employee;

/**
 * @author ngoc-anh
 *
 */
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
	Employee findById(int id);
	
	@Query("SELECT e FROM Employee e WHERE e.name LIKE :x")
	Page<Employee> search(@Param("x") String s, Pageable pageable);
}
