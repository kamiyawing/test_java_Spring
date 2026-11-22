package jp.co.sss.java_spring_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	
	// パスワード暗号化の「道具」をSpringに登録する
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// 勝手にログイン画面が出ないように前ページへのアクセスを許可する設定
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// 全てのページをアクセス許可
			// SpringSecurityでも簡単にアクセス制限できるが、今回ログイン機能を自作しているため、
			// ログイン有無の概念が別であるため、アクセス制限は別の方法を採用
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.csrf(csrf -> csrf.disable()); // 学習用のためCSRF保護をオフ
		return http.build();
	}
}
