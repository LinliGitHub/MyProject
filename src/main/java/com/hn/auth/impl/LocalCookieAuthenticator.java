package com.hn.auth.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hn.auth.Authenticator;
import com.hn.exception.AuthenticateException;
import com.hn.model.User;


public class LocalCookieAuthenticator implements Authenticator {

	public User authenticate(HttpServletRequest request, HttpServletResponse response) throws AuthenticateException {
		String cookie = getCookieFromRequest(request, "validToken");
		if (cookie == null) {
			return null;
		}
		//如果cookie不为空，验证该cookie是否正确
		return getUserByCookie(cookie);
	}

	private User getUserByCookie(String cookie) {
		// TODO check cookie is valid?
		//验证cookie是否有效与正确，如果正确有效，则返回当前用户
		return null;
		
	}

	private String getCookieFromRequest(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookieName.equalsIgnoreCase(cookie.getName())) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
