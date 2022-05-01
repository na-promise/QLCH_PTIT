/**
 * 
 */
package ptit.cnpm.qlch.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author ngoc-anh
 *
 */
@Entity
@Table(name = "employee")
@Data
public class Employee {
	@Id
	@Column(name = "employee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "employee_username")
	private String username;
	@Column(name = "employee_password")
	private String password;
	@Column(name = "employee_name")
	private String name;
	@Column(name = "employee_dateofbirth")
	private Date dateOfBirth;
	@Column(name = "employee_address")
	private String address;
}
