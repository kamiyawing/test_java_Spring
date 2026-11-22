package jp.co.sss.java_spring_test.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.java_spring_test.entity.Users;
import jp.co.sss.java_spring_test.form.LoginForm;
import jp.co.sss.java_spring_test.form.RegisterForm;
import jp.co.sss.java_spring_test.repository.UsersRepository;

@Controller
public class LoginController {
	
	private final UsersRepository repository;
	private final PasswordEncoder passwordEncoder;
	
	public LoginController(UsersRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	// ログイン画面
	@GetMapping("/userLogin")
	public String userLogin(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "user/Login";
	}

	// ログイン処理
	@PostMapping("/loginCheck")
	public String loginCheck(LoginForm form, HttpSession session, Model model) {

		Users user = repository.findByEmail(form.getEmail());
		
		if (user != null && passwordEncoder.matches(form.getPasswords(), user.getPasswords())) {
			session.setAttribute("user", user);
			return "redirect:/userTop";
		} else {
			model.addAttribute("error","メールアドレスもしくはパスワードが間違っています。");
			return "user/Login";
		}
	}	
	
	// 新規登録画面
	@PostMapping("/userRegister")
	public String userRegister(Model model) {
		model.addAttribute("registerForm", new RegisterForm());
		return "user/register";
	}
	
	// 新規登録処理
	@Transactional
	@PostMapping("/userRegister/create")
	public String userRegisterCreate(@Valid RegisterForm form, BindingResult result, RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {
			return "user/register";
		} 
		if (repository.findByEmail(form.getEmail()) != null) {
			result.rejectValue("email", null, "このメールアドレスは既に登録されています。");
			return "user/register";
		}
		Users users = new Users();
		BeanUtils.copyProperties(form, users, "userId");
		
		// BCryptで暗号化
		users.setPasswords(passwordEncoder.encode(form.getPasswords()));
		
		repository.save(users);
		redirectAttributes.addFlashAttribute("successMessage", "ユーザー登録が完了しました。");
		return "redirect:/userLogin";
	}
	
	// ログアウト
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/userLogin";
	}

}
