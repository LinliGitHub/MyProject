package com.hn.service;

import com.alibaba.fastjson.JSONObject;
import com.hn.dto.Session;
import com.hn.exception.RepeatSignUpException;
import com.hn.exception.SignUpException;

public interface UserService {
	/**
	 * 用户注册
	 * 
	 * @param user
	 */
	void signUp(String username, String password, String email, String phone)
			throws SignUpException, RepeatSignUpException;

	/**
	 * 校验用户名是否存在
	 * 
	 * @param username
	 * @return
	 */
	long existUser(String username);

	/**
	 * 用户登录
	 */
	Session signIn(String username, String password);

	/**
	 * 发送验证码
	 */
	JSONObject sendVerificationCode(String phone);

	/**
	 * 验证码验证
	 */
	boolean verifyVerificationCode(String phone, String code);
}
