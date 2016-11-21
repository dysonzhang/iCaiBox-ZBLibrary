package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

public class AuthMember extends BaseModel {

	private static final long serialVersionUID = 1L;

	private long userId;
	private String userHead;
	private String userName;
	private String userPhone;
	private String joinDate;
	// ‘0’表示未授权开箱，‘1’表示已经授权开箱
	private int authority;

	/**
	 * 默认构造方法，JSON等解析时必须要有
	 */
	public AuthMember() {
		// default
	}

	public AuthMember(long id) {
		this(id, null);
	}

	public AuthMember(String userName) {
		this(-1, userName);
	}

	public AuthMember(long id, String userName) {
		this.id = id;
		this.userName = userName;
	}

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

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	@Override
	public boolean isCorrect() {// 根据自己的需求决定，也可以直接 return true
		return id > 0;// && StringUtil.isNotEmpty(phone, true);
	}
}
