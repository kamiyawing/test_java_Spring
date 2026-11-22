package jp.co.sss.java_spring_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JavaSpringTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaSpringTestApplication.class, args);
	}

}
