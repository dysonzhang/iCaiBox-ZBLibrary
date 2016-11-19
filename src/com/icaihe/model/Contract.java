package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

public class Contract extends BaseModel {

	private static final long serialVersionUID = 1L;

	private long userId;
	private String userHead;
	private String userName;
	private String userPhone;
	private String joinDate;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	@Override
	public boolean isCorrect() {
		return true;
	}
}
