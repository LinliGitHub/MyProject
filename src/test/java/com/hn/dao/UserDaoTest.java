package com.hn.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hn.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class UserDaoTest {

	@Autowired
	private UserDao userDao;

	/**
	 * 单元测试-->注册功能整体逻辑
	 */
	// @Test
	// public void testSignUpLogic() {
	// User user = new User("tom", "tom", "/user/tom.icon", 0, new Date());
	// userDao.insertUser(user);
	// long id = user.getId();
	// System.out.println();
	// String username = "tom";
	// String password = "234567";
	// int insertAuth = userDao.insertAuth(id, username, password);
	// System.out.println(insertAuth > 0 ? "注册成功" : "注册失败");
	// }
	//
	// @Test
	// public void testSignIn() {
	// User signIn = userDao.signIn("tom", "234567");
	// System.out.println(signIn);
	// }
	//
	// @Test
	// public void testQueryUserById() {
	// User user = userDao.queryUserById(1);
	// System.out.println(user);
	// }

	@Test
	public void testQueryUserByName() {
		String username = "jack";
		
		long result = userDao.queryUserByName(username);
		System.out.println(result);
	}
}
