package com.hn.dto;

import com.hn.enums.UserStateEnum;
import com.hn.model.User;

/**
 * 用户登录会话dto
 * 
 * @author Linlihua
 *
 */
public class Session {
	private int state;
	private String stateInfo;
	private User user;
	private String token;

	public Session() {
		super();
	}

	public Session(UserStateEnum userStateEnum, User user) {
		this.state = userStateEnum.getState();
		this.stateInfo = userStateEnum.getStateInfo();
		this.user = user;
	}

	public Session(UserStateEnum userStateEnum, User user, String token) {
		this.state = userStateEnum.getState();
		this.stateInfo = userStateEnum.getStateInfo();
		this.user = user;
		this.token = token;
	}

	public User getUser() {
		return user;
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

	public void setUser(User user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "Session [state=" + state + ",stateinfo=" + stateInfo + " user=" + user + ", token=" + token + "]";
	}

}
