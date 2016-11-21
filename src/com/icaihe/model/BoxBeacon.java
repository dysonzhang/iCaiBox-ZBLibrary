package com.icaihe.model;

import zuo.biao.library.base.BaseModel;

public class BoxBeacon extends BaseModel {

	private static final long serialVersionUID = 1L;

	String beaconName;
	String UUID;
	String major;
	String minor;

	/**
	 * 默认构造方法，JSON等解析时必须要有
	 */
	public BoxBeacon() {
		// default
	}

	public BoxBeacon(long id) {
		this(id, null);
	}

	public BoxBeacon(String beaconName) {
		this(-1, beaconName);
	}

	public BoxBeacon(long id, String beaconName) {
		this.id = id;
		this.beaconName = beaconName;
	}

	public String getBeaconName() {
		return beaconName;
	}

	public void setBeaconName(String beaconName) {
		this.beaconName = beaconName;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMinor() {
		return minor;
	}

	public void setMinor(String minor) {
		this.minor = minor;
	}

	@Override
	public boolean isCorrect() {// 根据自己的需求决定，也可以直接 return true
		return id > 0;// && StringUtil.isNotEmpty(phone, true);
	}
}
