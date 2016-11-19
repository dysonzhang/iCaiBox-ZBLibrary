package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

public class Notice extends BaseModel {

	private static final long serialVersionUID = 1L;

	private String createTime;
	private String remark;
	private String userName;
	private int type;
	private String userHead;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}

	@Override
	public boolean isCorrect() {
		return true;
	}
}
