package com.hn.service;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.hn.dto.Exposer;
import com.hn.dto.SeckillExecution;
import com.hn.exception.RepeatKillException;
import com.hn.exception.SeckillCloseException;
import com.hn.model.Seckill;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class SeckillServiceTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;

	@Test
	public void testGetSeckillList() {
		List<Seckill> seckillList = seckillService.getSeckillList();
		logger.info("list={}", seckillList);
	}

	@Test
	public void testGetById() {
		long seckillId = 1;
		Seckill seckill = seckillService.getById(seckillId);
		logger.info("seckill={}", seckill);
	}

	@Test
	public void testExcuteLogic() {
		long seckillId = 1;
		Exposer export = seckillService.exportSeckillUrl(seckillId);
		if (export.isExposed()) {
			logger.info("export={}", export);
			try {
				long userPhone = 13423435374l;
				String md5 = export.getMd5();
				SeckillExecution excuteSeckill = seckillService.executeSeckill(seckillId, userPhone, md5);
				logger.info("excuteSeckill={}", excuteSeckill);
			} catch (SeckillCloseException e) {
				logger.error(e.getMessage());
			} catch (RepeatKillException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.warn("export={}", export);
		}

	}

	@Test
	public void testExcuteLogicProcedure() {
		long seckillId = 1;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		if (exposer.isExposed()) {
			logger.info("exposer:{}", exposer);
			long userPhone = 13423435374l;
			String md5 = exposer.getMd5();
			SeckillExecution seckillProcedure = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
			logger.info("seckillProcedure={}", seckillProcedure);
		} else {
			logger.warn("exposer={}", exposer);
		}
	}

}
