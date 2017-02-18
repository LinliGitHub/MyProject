package com.hn.dao;

import org.apache.ibatis.annotations.Param;

import com.hn.model.User;

/**
 * 用户信息相关操作
 * 
 * @author Linlihua
 *
 */
public interface UserDao {
	/**
	 * 注册新用户： 1:向用户信息表中插入记录 2:将用户名和密码及主键引用插入登录表
	 * 
	 * @param user
	 * @return
	 */
	long insertUser(User user);

	/**
	 * 将用户登录信息插入登录表
	 * 
	 * @param userId
	 * @param userName
	 * @param passWord
	 * @return
	 */
	int insertAuth(@Param("userId") long userId, @Param("username") String username,
			@Param("password") String password);

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	User signIn(@Param("username") String username, @Param("password") String password);

	/**
	 * 根据id查询用户信息
	 * 
	 * @param id
	 * @return
	 */
	User queryUserById(long id);

	/**
	 * 根据用户名查询用户是否注册
	 * 
	 * @param username
	 * @return
	 */
	long queryUserByName(@Param("username") String username);
}
