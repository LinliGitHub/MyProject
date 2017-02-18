package com.hn.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.hn.dto.Registration;
import com.hn.dto.Session;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
@Transactional
public class UserServiceTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private UserService userService;

	@Test
	public void testSignUpLogic() {
		boolean isExist = userService.existUser("tom") > 0;
		if (isExist) {
			logger.info("this username is exsist...");
			return;
		}
		userService.signUp("tom", "123456","123@qq.com","13234353653");
		//logger.info("signup={}" + );
	}

	@Test
	public void testSignIn() {
		Session signIn = userService.signIn("tom", "123456");
		logger.info("sign={}", signIn);
	}

}
