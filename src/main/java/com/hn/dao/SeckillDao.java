package com.hn.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hn.model.Seckill;

public interface SeckillDao {
	/**
	 * 减库存，createTime
	 * 
	 * @param seckillId
	 * @param killTime
	 * @return 如果影响行数为1则成功
	 *         mybatis查询或更新操作时如果有多个参数要传递的话，需要使用@Param("name")注解来指定参数的名称，
	 *         否则mybatis会将多个参数变为arg0,arg1等
	 */
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

	/**
	 * 根据id查询秒杀对象
	 * 
	 * @param seckillId
	 * @return
	 */
	Seckill queryById(long seckillId);

	/**
	 * 根据偏移量查询秒杀商品列表（以后可以自定义分页）
	 * 
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

	/**
	 * 
	 */
	void killByProcedure(Map<String, Object> params);
}
