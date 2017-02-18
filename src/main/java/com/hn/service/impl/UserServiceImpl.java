package com.hn.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hn.dao.UserDao;
import com.hn.dao.cache.RedisDao;
import com.hn.dto.Session;
import com.hn.enums.UserStateEnum;
import com.hn.exception.RepeatSignUpException;
import com.hn.exception.SignUpException;
import com.hn.model.User;
import com.hn.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisDao<User> redisDao;
	@Autowired
	private RedisDao<String> redisCommonDao;
	// 数据库密码盐值
	private final String salt = "md5_password_salt";
	// 生成标识cookie的盐值
	private final String salt_cookie = "md5_cookie_salt";

	@Transactional
	@Override
	public void signUp(String username, String password, String email, String phone)
			throws RepeatSignUpException, SignUpException {
		User user = new User();
		user.setUsername(username);
		user.setEmail(email);
		user.setPhone(phone);
		user.setIcon("/user/default/icon.icon");
		long insertUser = userDao.insertUser(user);
		try {
			if (insertUser == 0)
				throw new RepeatSignUpException(UserStateEnum.SIGNUP_REPEAT.getStateInfo());
			int insertAuth = userDao.insertAuth(user.getId(), username, getMD5(password, salt));
			if (insertAuth == 0)
				throw new SignUpException(UserStateEnum.SIGNUP_ERROR.getStateInfo());
			// 如果注册成功，把用户信息缓存起来
			String key = "user_" + user.getId();
			redisDao.set(key, User.class, user);
			//return new Registration(UserStateEnum.SIGNUP_SUCCESS, user);
		} catch (RepeatSignUpException re) {
			log.error("RepeatSignUpException:" + re.getMessage());
			throw re;
		} catch (SignUpException se) {
			log.error("SignUpException:" + se.getMessage());
			throw se;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SignUpException(UserStateEnum.SYS_ERROR.getStateInfo());
		}
	}

	@Override
	public Session signIn(String username, String password) {
		String secretPassword = getMD5(password, salt);
		User user = userDao.signIn(username, secretPassword);
		if (user == null)
			// 登录是个查询操作，不需要事务回滚，所以不需要抛出异常，只需要把失败信息返回即可
			return new Session(UserStateEnum.SIGNIN_ERROR, user);
		// 如果登录成功，则把用户信息存入缓存，并返回一个可信任cookie标识用户？
		long id = user.getId();
		String key = "user_" + id;
		User userCached = redisDao.get(key, User.class);
		if (userCached == null)
			redisDao.set(key, User.class, user);
		// unix时间戳
		long time = System.currentTimeMillis() / 1000 + 3600;
		String generateCookie = generateCookie(id, secretPassword, time);
		return new Session(UserStateEnum.SIGNIN_SUCCESS, user, generateCookie);
	}

	@Override
	public long existUser(String username) {
		long existed = userDao.queryUserByName(username);
		return existed;
	}

	/**
	 * 生成指定salt的md5值
	 **/
	private String getMD5(String password, String salt) {
		String base = password + "_" + salt;
		return DigestUtils.md5DigestAsHex(base.getBytes());
	}

	/**
	 * 生成验证器
	 * 
	 * @param userId
	 * @param password
	 * @param deadline
	 * @return
	 */
	private String generateValidator(long userId, String password, long deadline) {
		return getMD5(getConnectedStr(userId, password, deadline), salt_cookie);
	}

	/**
	 * 生成标识cookie
	 */
	private String generateCookie(long userId, String password, long deadline) {
		return getConnectedStr(userId, deadline, generateValidator(userId, password, deadline));
	}

	/**
	 * 获取拼接字符串
	 */
	private String getConnectedStr(Object... params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.length; i++) {
			sb.append(params[i]);
			if (i != params.length - 1)
				sb.append(":");
		}
		return sb.toString();
	}

	@Override
	public JSONObject sendVerificationCode(String phone) {
		// 检测手机号是否已存在
		long exsit = userDao.queryUserByName(phone);
		JSONObject result = new JSONObject();
		if (exsit == 0) {
			// 缓存5分钟
			String verificationCode = redisCommonDao.get(phone, String.class);
			if (StringUtils.isEmpty(verificationCode)) {
				// 一般是发http请求调用短信服务返回json结果后再将结果存入缓存
				verificationCode = "123456";
				redisCommonDao.set(phone, String.class, verificationCode);
			}
			result.put("code", 0);
			result.put("msg", "success");
		} else {
			result.put("code", UserStateEnum.SIGNUP_REPEAT.getState());
			result.put("msg", UserStateEnum.SIGNUP_REPEAT.getStateInfo());
		}
		return result;
	}

	public boolean verifyVerificationCode(String phone, String code) {
		String cacheCode = redisCommonDao.get(phone, String.class);
		return code.equals(cacheCode);
	}
}
