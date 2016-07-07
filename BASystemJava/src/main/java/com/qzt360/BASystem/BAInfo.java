package com.qzt360.BASystem;

import java.util.List;

public class BAInfo {
	private String strGroupCode;// 组织机构代码
	private String strName;// 名称
	private String strAddr;// 地址
	private String strZone;// 区域
	private String strUnitType;// 行业
	private List<EqpInfo> listEqp;// 设备列表

	public BAInfo() {

	}

	public BAInfo(String strGroupCode, String strName, String strAddr, String strZone, String strUnitType,
			List<EqpInfo> listEqp) {
		super();
		this.strGroupCode = strGroupCode;
		this.strName = strName;
		this.strAddr = strAddr;
		this.strZone = strZone;
		this.strUnitType = strUnitType;
		this.listEqp = listEqp;
	}

	public BAInfo(String strGroupCode, String strName, String strAddr, String strZone, String strUnitType) {
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

	public void setStrGroupCode(String strGroupCode) {
		this.strGroupCode = strGroupCode;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrAddr() {
		return strAddr;
	}

	public void setStrAddr(String strAddr) {
		this.strAddr = strAddr;
	}

	public String getStrZone() {
		return strZone;
	}

	public void setStrZone(String strZone) {
		this.strZone = strZone;
	}

	public String getStrUnitType() {
		return strUnitType;
	}

	public void setStrUnitType(String strUnitType) {
		this.strUnitType = strUnitType;
	}

	public List<EqpInfo> getListEqp() {
		return listEqp;
	}

	public void setListEqp(List<EqpInfo> listEqp) {
		this.listEqp = listEqp;
	}

}
