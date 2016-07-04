package com.qzt360.BASystem;

public class UnitInfo {
	private final String strGroupCode;// 组织机构代码
	private final String strName;// 名称
	private final String strAddr;// 地址
	private final String strZone;// 区域
	private final String strUnitType;// 行业

	public UnitInfo(String strGroupCode, String strName, String strAddr, String strZone, String strUnitType) {
		super();
		this.strGroupCode = strGroupCode;
		this.strName = strName;
		this.strAddr = strAddr;
		this.strZone = strZone;
		this.strUnitType = strUnitType;
	}
	
	public String getStrGroupCode() {
		return strGroupCode;
	}

	public String getStrName() {
		return strName;
	}

	public String getStrAddr() {
		return strAddr;
	}

	public String getStrZone() {
		return strZone;
	}

	public String getStrUnitType() {
		return strUnitType;
	}

}
