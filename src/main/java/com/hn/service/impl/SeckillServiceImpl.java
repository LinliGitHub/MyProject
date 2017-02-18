package com.hn.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import com.hn.dao.SeckillDao;
import com.hn.dao.SuccessKilledDao;
import com.hn.dao.cache.RedisDao;
import com.hn.dto.Exposer;
import com.hn.dto.SeckillExecution;
import com.hn.enums.SeckillStateEnum;
import com.hn.exception.RepeatKillException;
import com.hn.exception.SeckillCloseException;
import com.hn.exception.SeckillException;
import com.hn.model.Seckill;
import com.hn.model.Successkilled;
import com.hn.service.SeckillService;

//
//@Component
@Service
public class SeckillServiceImpl implements SeckillService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	// 盐值
	private final String slat = "jlfsaj332413";
	// 注入service依赖
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private RedisDao<Seckill> redisDao;
	@Autowired
	private SuccessKilledDao successKilledDao;

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 10);
	}

	public Seckill getById(long id) {
		return seckillDao.queryById(id);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		/*
		 * 缓存优化： 维护一致性是基于超时的缓存上的
		 */
		String key="seckill"+seckillId;
		Seckill seckill = redisDao.get(key,Seckill.class);
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				redisDao.set(key,Seckill.class,seckill);
			}
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date now = new Date();
		if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Transactional
	/**
	 * 使用注解事务优点： 1:开发团队达成一致约定，明确标注注解事务的编程风格
	 * 2:保证事务方法的执行时间尽可能短，不要穿插其他网络操作请求如http/rpc等或者剥离其他网络操作到该事务方法外
	 * 3:不是所有的方法都需要事务，如只有一条数据修改或者是只读操作不需要事务，
	 */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, SeckillCloseException, RepeatKillException {
		if (md5 == null || !getMD5(seckillId).equals(md5)) {
			throw new SeckillException("seckill data rewrite");
		}
		try {
			// 记录购买行为
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			if (insertCount <= 0) {
				throw new RepeatKillException("seckill repeated");
			} else {
				// 执行秒杀:减库存和记录购买行为
				int updateCount = seckillDao.reduceNumber(seckillId, new Date());
				if (updateCount <= 0) {
					// 秒杀结束
					throw new SeckillCloseException("seckill is closed");
				} else {
					Successkilled successkilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successkilled);
				}

			}

		} catch (SeckillCloseException ec) {
			throw ec;
		} catch (RepeatKillException re) {
			throw re;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SeckillException("seckill inner exception" + e.getMessage());
		}
	}

	public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
		if (md5 == null && !getMD5(seckillId).equals(md5)) {
			return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("seckillId", seckillId);
		params.put("userPhone", userPhone);
		params.put("killTime", killTime);
		params.put("result", null);
		try {
			seckillDao.killByProcedure(params);
			Integer result = MapUtils.getInteger(params, "result", -2);
			if (result == 1) {
				Successkilled successkilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successkilled);
			} else {
				return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStateEnum.CLOSE);
		}
	}
}
