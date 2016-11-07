package com.qzt360.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqpInfo {
	private String strSN;
	private String strEqpId;
	private String strUnitName;
	private String strUnitNameM;
	private String strUnitAddr;
	private String strUnitAddrM;
	private String strUnitLinkman;
	private String strUnitLinkmanM;
	private String strUnitLinkmanTel;
	private String strUnitLinkmanTelM;
	private String strBusinessLicense;
	private String strBusinessLicenseM;
	private String strUnitZoneCode;
	private String strUnitZoneName;
	private String strUnitCode;
	private String strLng;
	private String strLat;
	private boolean bBAStatus;
	private String dtBATime;
	private String dtUpdateTimeM;
	private boolean bDataStatus;
	private String dtCreateTime;
	// 客服需要的备注字段
	private String strMemo;
	private int nActionType;// 动作类型,1 信息确认 2 取消确认 3 保存
	private boolean bFeedback;// 是否需要反馈
	private String strOperator;// 回访人
	private boolean bConfirmStatus;// 是否手工确认

}
