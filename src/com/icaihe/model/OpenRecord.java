package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

public class OpenRecord extends BaseModel {

	private static final long serialVersionUID = 1L;

	// "createTime": "2016-08-26 11:11:07",
	// "remark": "开了财盒",
	// "userName": "lhy",
	// "type": 1,
	// "userHead": "1myg.jpg"

	private String userHead;
	private String userName;
	private String createTime;
	private String remark;
	private int type;

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
