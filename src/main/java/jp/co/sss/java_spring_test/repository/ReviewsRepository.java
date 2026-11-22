package jp.co.sss.java_spring_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.java_spring_test.entity.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {
	
	List<Reviews> findByProductIdOrderByCreatedAtDesc(Integer productId);
	
}
