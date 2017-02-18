package com.hn.model;

import java.util.Date;

public class User {
	/* 主键 */
	private long id;
	/* 用户名 */
	private String username;
	/* 昵称 */
	private String nickname;
	/* 头像 */
	private String icon;
	/* 是否启用 */
	private int inUsed;
	/* 创建时间 */
	private Date createTime;
	/* email */
	private String email;
	/* phone */
	private String phone;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public User() {
		super();
	}

	public User(String username, String nickname, String icon, int inUsed, Date createTime, String email) {
		super();
		this.username = username;
		this.nickname = nickname;
		this.icon = icon;
		this.inUsed = inUsed;
		this.createTime = createTime;
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getInUsed() {
		return inUsed;
	}

	public void setInUsed(int inUsed) {
		this.inUsed = inUsed;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ",email=" + email + ",phone=" + phone + " nickname="
				+ nickname + "icon=" + icon + "createTime=" + createTime + "]";
	}

}
