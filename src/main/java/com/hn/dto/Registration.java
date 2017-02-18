package com.hn.dto;

import com.hn.enums.UserStateEnum;
import com.hn.model.User;

/**
 * 用户注册结果封装dto
 * 
 * @author Linlihua
 *
 */
public class Registration {
	// 是否注册
	private int state;
	private String stateInfo;
	// 当前注册的用户
	private User user;

	public Registration() {
	}

	public Registration(UserStateEnum userStateEnum, User user) {
		super();
		this.state = userStateEnum.getState();
		this.stateInfo = userStateEnum.getStateInfo();
		this.user = user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Registration [ state=" + state + ", stateInfo=" + stateInfo + ", User=" + user + "]";
	}

}
