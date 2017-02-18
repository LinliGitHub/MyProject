package com.hn.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hn.dao.SeckillDao;
import com.hn.model.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit配置spring文件
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class RedisDaoTest {
	@Autowired
	private RedisDao<Seckill> redisDao;
	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void testSeckill() {
		long seckillId = 1;
		String key = "seckill_" + seckillId;
		Seckill seckill = redisDao.get(key, Seckill.class);
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			if (seckill != null) {
				String result = redisDao.set(key, Seckill.class, seckill);
				System.out.println(result);
				seckill = redisDao.get(key, Seckill.class);
				System.out.println(seckill);
			}
		}

	}
}
