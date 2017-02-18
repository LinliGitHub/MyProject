package com.hn.dao;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hn.model.Successkilled;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
	@Autowired
	private SuccessKilledDao successkilledDao;
	@Test
	public void testInsertSuccessKilled() {
		long seckillId=1;
		long userPhone=13423435374l;
		int successKilled = successkilledDao.insertSuccessKilled(seckillId, userPhone);
		System.out.println(successKilled);
	}

	@Test
	public void testQueryByIdWithSeckill() {
		Successkilled queryByIdWithSeckill = successkilledDao.queryByIdWithSeckill(1,13423435374l);
		System.out.println(queryByIdWithSeckill.getSeckill());
	}

}
