package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

/**
 * 用户类
 */
public class Group extends BaseModel {

	private static final long serialVersionUID = 1L;

	// "groupId": 3,
	// "groupName": "ceshi"

	String groupName;

	/**
	 * 默认构造方法，JSON等解析时必须要有
	 */
	public Group() {
		// default
	}

	public Group(long id) {
		this(id, null);
	}

	public Group(String groupName) {
		this(-1, groupName);
	}

	public Group(long id, String groupName) {
		this.id = id;
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public boolean isCorrect() {// 根据自己的需求决定，也可以直接 return true
		return id > 0;// && StringUtil.isNotEmpty(phone, true);
	}
}
