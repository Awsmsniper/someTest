package org.after90.JavaAlgorithm;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum IdType {

	QQ("1002", "QQ"), Email("1009", "Email");

	private String strCode;
	private String strName;

	public static String getTypeName(String strCode) {
		for (IdType idType : IdType.values()) {
			if (idType.getStrCode().equals(strCode)) {
				return idType.getStrName();
			}
		}
		return "-";
	}

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
