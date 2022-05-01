/**
 * 
 */
package ptit.cnpm.qlch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ptit.cnpm.qlch.entity.Product;

/**
 * @author ngoc-anh
 *
 */
public interface ProductRepo extends JpaRepository<Product, Integer> {
	Product findById(int id);

	@Query("SELECT p FROM Product p WHERE p.name LIKE :x")
	Page<Product> search(@Param("x") String s, Pageable pageable);
}
