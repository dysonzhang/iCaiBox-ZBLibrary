package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

public class GroupMember extends BaseModel {

	private static final long serialVersionUID = 1L;

	// "joinDate": null,
	// "userId": 1,
	// "userName": "lhy",
	// "type": 0,
	// "userHead": "1myg.jpg"

	private long userId;
	private String userHead;
	private String userName;
	private String joinDate;
	private int type;

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

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public boolean isCorrect() {// 根据自己的需求决定，也可以直接 return true
		return true;// && StringUtil.isNotEmpty(phone, true);
	}
}
