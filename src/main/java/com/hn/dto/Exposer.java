package com.hn.dto;

/**
 * 暴露秒杀地址doto
 * 
 * @author Linlihua
 *
 */
public class Exposer {
	// 是否开启秒杀
	private boolean exposed;
	// 一种加密措施
	private String md5;
	// id
	private long seckillId;
	// 服务器系统当前时间
	private long now;
	// 开始时间
	private long start_time;
	// 结束时间
	private long end_time;

	public Exposer(boolean exposed, String md5, long seckillId) {
		super();
		this.exposed = exposed;
		this.md5 = md5;
		this.seckillId = seckillId;
	}

	public Exposer(boolean exposed, long now, long seckillId, long start_time, long end_time) {
		super();
		this.exposed = exposed;
		this.now = now;
		this.seckillId = seckillId;
		this.start_time = start_time;
		this.end_time = end_time;
	}

	public Exposer(boolean exposed, long seckillId) {
		super();
		this.exposed = exposed;
		this.seckillId = seckillId;
	}

	public boolean isExposed() {
		return exposed;
	}

	public void setExposed(boolean exposed) {
		this.exposed = exposed;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public long getNow() {
		return now;
	}

	public void setNow(long now) {
		this.now = now;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	@Override
	public String toString() {
		return "Exposer [exposed=" + exposed + ", md5=" + md5 + ", seckillId=" + seckillId + ", now=" + now
				+ ", start_time=" + start_time + ", end_time=" + end_time + "]";
	}

}
