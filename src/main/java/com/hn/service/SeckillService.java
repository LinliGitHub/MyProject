package com.hn.service;

import java.util.List;

import com.hn.dto.Exposer;
import com.hn.dto.SeckillExecution;
import com.hn.exception.RepeatKillException;
import com.hn.exception.SeckillCloseException;
import com.hn.exception.SeckillException;
import com.hn.model.Seckill;

/**
 * 站在“使用者”角度设计接口
 * @author Linlihua 三个方面：方法定义粒度，参数越明确越好，返回类型/异常
 */
public interface SeckillService {
	/**
	 * 查询所有秒杀商品列表
	 * 
	 * @return
	 */
	List<Seckill> getSeckillList();

	/**
	 * 根据id查询指定秒杀商品
	 * 
	 * @param id
	 * @return
	 */
	Seckill getById(long id);

	/**
	 * 秒杀开始时：输出秒杀接口地址 否则输出系统当前时间与秒杀时间
	 */
	Exposer exportSeckillUrl(long seckillId);

	/**
	 * 执行秒杀操作
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, SeckillCloseException, RepeatKillException;
	/**
	 * 执行秒杀操作（储存过程实现）
	 */
	SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
