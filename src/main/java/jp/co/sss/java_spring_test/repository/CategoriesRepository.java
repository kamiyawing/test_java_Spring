package jp.co.sss.java_spring_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.java_spring_test.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

}
