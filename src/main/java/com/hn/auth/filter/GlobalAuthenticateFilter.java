package com.hn.auth.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hn.auth.Authenticator;
import com.hn.auth.context.UserContext;
import com.hn.model.User;

public class GlobalAuthenticateFilter implements Filter {
	// 所有的Authenticator都在这里:
	Authenticator[] authenticators = initAuthenticators();

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	// 初始化验证器
	private Authenticator[] initAuthenticators() {
		// TODO:init authenticators
		return null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 链式认证获得User:
		User user = tryGetAuthenticatedUser(request, response);
		// 把User绑定到UserContext中:
		try (UserContext ctx = new UserContext(user)) {
			chain.doFilter(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private User tryGetAuthenticatedUser(ServletRequest request, ServletResponse response) {
		User user = null;
		for (Authenticator auth : this.authenticators) {
			user = auth.authenticate((HttpServletRequest) request, (HttpServletResponse) response);
			if (user != null) {
				break;
			}
		}
		return user;
	}

	public void destroy() {
		UserContext.current.remove();
	}

}
