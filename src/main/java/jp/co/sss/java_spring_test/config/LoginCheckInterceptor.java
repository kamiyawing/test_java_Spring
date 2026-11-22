package jp.co.sss.java_spring_test.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// セッションを取得
		HttpSession session = request.getSession();
		
		// セッションに"user"という名前のデータがあるかチェック
		if (session.getAttribute("user") == null) {
			// なければログイン画面にリダイレクト
			response.sendRedirect(request.getContextPath() + "/userLogin");
			return false;
		}
		
		// あればそのまま次の処理
		return true;
		
	}
}
