package jp.co.sss.java_spring_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.java_spring_test.entity.Carts;
import jp.co.sss.java_spring_test.entity.Products;
import jp.co.sss.java_spring_test.entity.Users;

public interface CartsRepository extends JpaRepository<Carts, Integer> {
	
	Carts findByUserAndProduct(Users user, Products product);
	
	List<Carts> findByUser(Users user);

}
