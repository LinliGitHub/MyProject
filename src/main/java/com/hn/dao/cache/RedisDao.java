package com.hn.dao.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis dao缓存数据操作也属于dao操作
 * 
 * @author Linlihua
 *
 */
public class RedisDao<T> /* implements Closeable */ {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final JedisPool jedisPoll;

	public RedisDao(String host, int port) {
		jedisPoll = new JedisPool(host, port);
	}

	/**
	 * 改写存redis缓存
	 * 
	 * @param 缓存key
	 * @param 缓存class
	 *            如String.class
	 * @param t
	 *            缓存对象
	 * @return
	 */
	public String set(String key, Class<T> clazz, T t) {
		Jedis jedis = jedisPoll.getResource();
		try {
			RuntimeSchema<T> schema = RuntimeSchema.createFrom(clazz);
			byte[] bs = ProtobufIOUtil.toByteArray(t, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
			int timeout = 60 * 60;
			return jedis.setex(key.getBytes(), timeout, bs);
		} finally {
			jedis.close();
		}
	}

	/**
	 * 改写取redis缓存
	 * 
	 * @param 缓存key
	 * @param 缓存class
	 *            如String.class
	 * @return
	 */

	public T get(String key, Class<T> clazz) {
		try {
			Jedis jedis = jedisPoll.getResource();
			try {
				byte[] bs = jedis.get(key.getBytes());
				// 缓存中取到了该对象
				if (bs != null) {
					RuntimeSchema<T> schema = RuntimeSchema.createFrom(clazz);
					T t = schema.newMessage();
					ProtobufIOUtil.mergeFrom(bs, t, schema);
					return t;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
