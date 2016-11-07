package com.qzt360.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BAInfo {

	private String strUnitName = "";// 场所名称
	private String strUnitLinkman = "";// 联系人
	private String strUnitLinkmanTel = "";// 电话
	private String strUnitAddr = "";// 安装地址
	private String strUnitPostcode = "";// 邮编
	private String strUnitFax = "";// 传真
	private String strCustmerManager = "";// 电信客户经理
	private String strCustmerManagerTel = "";// 电话
	private String strUnitType = "";// 客户类型
	private String strAccessType = "";// 上网方式
	private String strBusinessLicense = "";// 营业执照编号
	private String strCorporateID = "";// 法人身份证
	private String strCorporateTel = "";// 电话
	private String strRoomNum = "";// 设备安装房间号
	private String strCreateTime = "";// 数据入库时间
	private String strUnitZone = "";// 所属区域
	private List<EqpInfoBA> listEqp = new ArrayList<EqpInfoBA>();// 设备列表

	public BAInfo(String strUnitName, String strUnitLinkman, String strUnitLinkmanTel, String strUnitAddr,
                  String strUnitPostcode, String strUnitFax, String strCustmerManager, String strCustmerManagerTel,
                  String strUnitType, String strAccessType, String strBusinessLicense, String strCorporateID,
                  String strCorporateTel, String strRoomNum, String strCreateTime, String strUnitZone) {
		super();
		this.strUnitName = strUnitName;
		this.strUnitLinkman = strUnitLinkman;
		this.strUnitLinkmanTel = strUnitLinkmanTel;
		this.strUnitAddr = strUnitAddr;
		this.strUnitPostcode = strUnitPostcode;
		this.strUnitFax = strUnitFax;
		this.strCustmerManager = strCustmerManager;
		this.strCustmerManagerTel = strCustmerManagerTel;
		this.strUnitType = strUnitType;
		this.strAccessType = strAccessType;
		this.strBusinessLicense = strBusinessLicense;
		this.strCorporateID = strCorporateID;
		this.strCorporateTel = strCorporateTel;
		this.strRoomNum = strRoomNum;
		this.strCreateTime = strCreateTime;
		this.strUnitZone = strUnitZone;
	}

}
