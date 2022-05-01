/**
 * 
 */
package ptit.cnpm.qlch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptit.cnpm.qlch.entity.Category;

/**
 * @author ngoc-anh
 *
 */
public interface CategoryRepo extends JpaRepository<Category, Integer> {
	Category findById(int id);

	@Query("SELECT c FROM Category c WHERE c.name LIKE :x")
	Page<Category> search(@Param("x") String s, Pageable pageable);
}
