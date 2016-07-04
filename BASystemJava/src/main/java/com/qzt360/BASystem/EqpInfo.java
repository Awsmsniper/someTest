package com.qzt360.BASystem;

public class EqpInfo {
	private final String strId;// 设备ID
	private final String strDataStatus;// 数据状态
	private final String strBAStatus;// 备案状态
	private final String strBATime;// 备案时间

	public EqpInfo(String strId, String strDataStatus, String strBAStatus, String strBATime) {
		super();
		this.strId = strId;
		this.strDataStatus = strDataStatus;
		this.strBAStatus = strBAStatus;
		this.strBATime = strBATime;
	}

	public String getStrId() {
		return strId;
	}

	public String getStrDataStatus() {
		return strDataStatus;
	}

	public String getStrBAStatus() {
		return strBAStatus;
	}

	public String getStrBATime() {
		return strBATime;
	}

}
