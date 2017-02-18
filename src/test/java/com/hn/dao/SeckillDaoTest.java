package com.hn.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hn.model.Seckill;

/**
 * 配置 spring 和 junit4整合，junit启动时加载springIOC容器
 * 
 * @author Linlihua
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit配置spring文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SeckillDaoTest {

	// 注入dao依赖
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testQueryById() {
		long id = 1;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}

	@Test
	public void testReduceNumber() {
		Date createTime = new Date();
		int reduceNumber = seckillDao.reduceNumber(1, createTime);
		System.out.println(reduceNumber);
	}

	@Test
	public void testQueryAll() {
		List<Seckill> list = seckillDao.queryAll(0, 10);
		for (Seckill sk : list) {
			System.out.println(sk);
		}
	}

}
