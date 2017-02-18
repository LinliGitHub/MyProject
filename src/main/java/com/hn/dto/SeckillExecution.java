package com.hn.dto;

import com.hn.enums.SeckillStateEnum;
import com.hn.model.Successkilled;

/**
 * 封装秒杀执行结果
 * 
 * @author Linlihua
 *
 */
public class SeckillExecution {
	private long seckillId;
	private int state;
	private String stateInfo;
	private Successkilled successkilled;

	public SeckillExecution(long seckillId, SeckillStateEnum seckillStateEnum, Successkilled successkilled) {
		super();
		this.seckillId = seckillId;
		this.state = seckillStateEnum.getState();
		this.stateInfo = seckillStateEnum.getStateInfo();
		this.successkilled = successkilled;
	}

	public SeckillExecution(long seckillId, SeckillStateEnum seckillStateEnum) {
		super();
		this.seckillId = seckillId;
		this.state = seckillStateEnum.getState();
		this.stateInfo = seckillStateEnum.getStateInfo();
	}

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public Successkilled getSuccesskilled() {
		return successkilled;
	}

	public void setSuccesskilled(Successkilled successkilled) {
		this.successkilled = successkilled;
	}

	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state + ", stateInfo=" + stateInfo
				+ ", successkilled=" + successkilled + "]";
	}
}
