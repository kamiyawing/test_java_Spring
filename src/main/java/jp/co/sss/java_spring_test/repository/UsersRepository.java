package jp.co.sss.java_spring_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.java_spring_test.entity.Users;


public interface UsersRepository extends JpaRepository<Users, Integer> {

	Users findByEmailAndPasswords(String email, String passwords);
	Users findByEmail(String email);

}
