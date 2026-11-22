package jp.co.sss.java_spring_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.java_spring_test.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Integer> {

	List<Products> findByProductNameContaining(String productName);
	
	List<Products> findByCategoryId(Integer categoryId);
	
	List<Products> findByProductNameContainingAndCategoryId(String productName,Integer categoryId);
}
