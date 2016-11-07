package com.qzt360.service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IdTypeEnum {
	QQ3011("1030001", "qq"), Weixin3011("1030036", "weixin"), WeiboTencent3011("1330002", "weiboTecent"), WeiboSina3011(
			"1330001", "weiboSina"), EmailPop33011("1010002", "emailPop3"), Tel3011("1020004",
					"tel"), Mac("1020002", "mac"), TaobaoWangwang("1030022", "wangwang");

	private String strCode;
	private String strName;

	public String getStrCode() {
		return strCode;
	}

	public void setStrCode(String strCode) {
		this.strCode = strCode;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

}
