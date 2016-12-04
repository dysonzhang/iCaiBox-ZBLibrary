package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

/**
 * 用户类
 */
public class User extends BaseModel {

	private static final long serialVersionUID = 1L;

	String token; // "A6kK5pVdU42UMZF65uInSrflS6r1sTRO7pKDaiaiXSs%3D"
	long userId; // 用户id
	String name; // 用户姓名
	String phone; // 用户手机号
	String alarmNum;// 表示未读报警记录数目
	boolean isNewUser; // 如果是群组创建人或者群组成员，此处为false；如果没有创建群组，也没有加入某个群组，此处为true。
	long groupId;// 所在群组ID
	String companyName;// 所在群组名称
	long boxId;// 具有开箱权限的财盒的ID
	String wifiId;// 财盒绑定的无线网的名称
	boolean isGroupCreator;//是否是群主 true是 false否 目前只有群主可以添加财盒，修改重新配置，授权

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAlarmNum() {
		return alarmNum;
	}

	public void setAlarmNum(String alarmNum) {
		this.alarmNum = alarmNum;
	}

	public boolean isNewUser() {
		return isNewUser;
	}

	public void setNewUser(boolean isNewUser) {
		this.isNewUser = isNewUser;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public long getBoxId() {
		return boxId;
	}

	public void setBoxId(long boxId) {
		this.boxId = boxId;
	}

	public String getWifiId() {
		return wifiId;
	}

	public void setWifiId(String wifiId) {
		this.wifiId = wifiId;
	}

	public boolean isGroupCreator() {
		return isGroupCreator;
	}

	public void setGroupCreator(boolean isGroupCreator) {
		this.isGroupCreator = isGroupCreator;
	}

	@Override
	public boolean isCorrect() {// 根据自己的需求决定，也可以直接 return true
		return true;// && StringUtil.isNotEmpty(phone, true);
	}

}
