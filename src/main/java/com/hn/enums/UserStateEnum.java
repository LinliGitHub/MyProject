package com.hn.enums;

/**
 * 使用枚举保存常量数据
 * 
 * @author Linlihua
 *
 */
public enum UserStateEnum {
	SIGNUP_SUCCESS(0, "注册成功"), SIGNUP_REPEAT(1, "重复注册"), SIGNUP_ERROR(3, "注册失败"), SIGNIN_SUCCESS(4,
			"登录成功"), SIGNIN_ERROR(5, "登录失败"),SYS_ERROR(6,"系统异常"),PARAM_VALID(7,"验证通过");
	private int state;
	private String stateInfo;

	private UserStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static UserStateEnum stateof(int index) {
		for (UserStateEnum state : UserStateEnum.values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
