package com.hn.dao;

import org.apache.ibatis.annotations.Param;

import com.hn.model.Successkilled;

public interface SuccessKilledDao {
	/**
	 * 插入秒杀明细,可过滤重复（复合主键）
	 * @param seckillId
	 * @param userPhone
	 */
	int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
	/**
	 * 查询Successkilled,并携带Seckill
	 * @param seckillId
	 * @return
	 */
	Successkilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
