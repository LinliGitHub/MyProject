package com.hn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hn.dto.SeckillResult;
import com.hn.dto.Session;
import com.hn.enums.UserStateEnum;
import com.hn.exception.RepeatSignUpException;
import com.hn.exception.SignUpException;
import com.hn.service.UserService;

/**
 * 账户模块
 * 
 * @author Linlihua
 *
 */
@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private UserService userService;

	/**
	 * 用户信息默认首页
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				System.out.println(cookie.getName() + ":" + cookie.getValue());
			}
		}
		return "index";
	}

	/**
	 * 登录页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "return_to", required = false) String return_to, Model model) {
		model.addAttribute("return_to", return_to);
		return "login";
	}

	/**
	 * 登录会话
	 */
	@RequestMapping(value = "/session", method = RequestMethod.POST)
	public String signin(HttpServletResponse response, String login, String password, String return_to, Model model) {
		// 登录逻辑
		Session session = userService.signIn(login, password);
		// 默认跳转url
		String redirect_url = "/account/index";
		if (UserStateEnum.SIGNIN_SUCCESS.getState() == session.getState()) {
			// cookie
			String token = session.getToken();
			Cookie cookie = new Cookie("validToken", token);
			cookie.setPath("/");
			response.addCookie(cookie);
			if (return_to != null)
				redirect_url = return_to;
		} else {
			model.addAttribute("msg", 1);
			model.addAttribute("return_to", return_to);
			return "login";
		}
		return "redirect:" + redirect_url;
	}

	/**
	 * 注册检查
	 */
	@RequestMapping(value = "/{username}/signup_check", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Long> signUpCheck(@PathVariable("username") String username) {
		long existent = userService.existUser(username);
		return new SeckillResult<Long>(true, existent);
	}

	/**
	 * 发送验证码
	 */
	@RequestMapping(value = "/{phone}/transmission", method = RequestMethod.POST, produces = {
			"application/json;chartset=UTF-8" })
	@ResponseBody
	public SeckillResult<JSONObject> sendCode(@PathVariable("phone") String phone) {
		// 手机发送验证码,一般是发送短信机，返回后存入缓存5分钟，如果缓存中没有该验证码则再次生成
		JSONObject result = userService.sendVerificationCode(phone);
		return new SeckillResult<JSONObject>(true, result);
	}

	/**
	 * 验证验证码
	 */
	@RequestMapping(value = "/verification", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<Boolean> verification(String code, String phone) {
		boolean flag = userService.verifyVerificationCode(phone, code);
		return new SeckillResult<Boolean>(true, flag);
	}

	/**
	 * 注册,需要判断是显示页面还是处理请求
	 */
	@RequestMapping(value = "/join", method = { RequestMethod.POST, RequestMethod.GET })
	public String signup(String username, String password, String email, String phone, Model model,
			HttpServletRequest request) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(email)) {
			return "join";
		}
		// deal with sign up logic
		try {
			userService.signUp(username, password, email, phone);
		} catch (RepeatSignUpException e) {
			// 报错,添加说明和跳转url，进入提示页面
			model.addAttribute("redirect_url", request.getRequestURL());
			model.addAttribute("msg", "重复注册");
			return "common/prompt";
		} catch (SignUpException e) {
			model.addAttribute("msg", "注册失败");
			model.addAttribute("redirect_url", request.getRequestURL());
			return "common/prompt";
		}
		return "redirect:/account/login";
	}

}
