package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

public class Group extends BaseModel {

	private static final long serialVersionUID = 1L;

	long groupId;
	String groupName;

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public boolean isCorrect() {// 根据自己的需求决定，也可以直接 return true
		return true;// && StringUtil.isNotEmpty(phone, true);
	}
}
