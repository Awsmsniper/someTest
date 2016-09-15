package com.qzt360.esTest;

public class IMManager {

	private int nCustomerId;
	private int nDeptId;
	private String strHostName;
	private String strEqpName;
	private int nTime;
	private long lSip;
	private String strSmac;
	private int nApplication;
	private int nType;
	private long lSessionId;
	private String strCallerId;
	private String strCalledId;
	private int nDir;
	private int nContentLen;
	private String strContent;
	private int nPartyNum;
	private String strCallerNickname;
	private String strCalledNickname;
	private int nCertType;
	private String strCertCode;
	private String strName;
	private String strCountryCode;

	public boolean isLegal(String strLine) {
		String[] astrLine = (strLine + "\tendMark").split("\t");
		if (astrLine.length == 23) {
			try {
				nCustomerId = Integer.parseInt(astrLine[0]);
				nDeptId = Integer.parseInt(astrLine[1]);
				strHostName = astrLine[2];
				strEqpName = astrLine[3];
				nTime = Integer.parseInt(astrLine[4]);
				lSip = Long.parseLong(astrLine[5]);
				strSmac = astrLine[6];
				nApplication = Integer.parseInt(astrLine[7]);
				nType = Integer.parseInt(astrLine[8]);
				lSessionId = Long.parseLong(astrLine[9]);
				strCallerId = astrLine[10];
				strCalledId = astrLine[11];
				nDir = Integer.parseInt(astrLine[12]);
				nContentLen = Integer.parseInt(astrLine[13]);
				strContent = astrLine[14];
				nPartyNum = Integer.parseInt(astrLine[15]);
				strCallerNickname = astrLine[16];
				strCalledNickname = astrLine[17];
				nCertType = Integer.parseInt(astrLine[18]);
				strCertCode = astrLine[19];
				strName = astrLine[20];
				strCountryCode = astrLine[21];
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public int getnCustomerId() {
		return nCustomerId;
	}

	public void setnCustomerId(int nCustomerId) {
		this.nCustomerId = nCustomerId;
	}

	public int getnDeptId() {
		return nDeptId;
	}

	public void setnDeptId(int nDeptId) {
		this.nDeptId = nDeptId;
	}

	public String getStrHostName() {
		return strHostName;
	}

	public void setStrHostName(String strHostName) {
		this.strHostName = strHostName;
	}

	public String getStrEqpName() {
		return strEqpName;
	}

	public void setStrEqpName(String strEqpName) {
		this.strEqpName = strEqpName;
	}

	public int getnTime() {
		return nTime;
	}

	public void setnTime(int nTime) {
		this.nTime = nTime;
	}

	public long getlSip() {
		return lSip;
	}

	public void setlSip(long lSip) {
		this.lSip = lSip;
	}

	public String getStrSmac() {
		return strSmac;
	}

	public void setStrSmac(String strSmac) {
		this.strSmac = strSmac;
	}

	public int getnApplication() {
		return nApplication;
	}

	public void setnApplication(int nApplication) {
		this.nApplication = nApplication;
	}

	public int getnType() {
		return nType;
	}

	public void setnType(int nType) {
		this.nType = nType;
	}

	public long getlSessionId() {
		return lSessionId;
	}

	public void setlSessionId(long lSessionId) {
		this.lSessionId = lSessionId;
	}

	public String getStrCallerId() {
		return strCallerId;
	}

	public void setStrCallerId(String strCallerId) {
		this.strCallerId = strCallerId;
	}

	public String getStrCalledId() {
		return strCalledId;
	}

	public void setStrCalledId(String strCalledId) {
		this.strCalledId = strCalledId;
	}

	public int getnDir() {
		return nDir;
	}

	public void setnDir(int nDir) {
		this.nDir = nDir;
	}

	public int getnContentLen() {
		return nContentLen;
	}

	public void setnContentLen(int nContentLen) {
		this.nContentLen = nContentLen;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}

	public int getnPartyNum() {
		return nPartyNum;
	}

	public void setnPartyNum(int nPartyNum) {
		this.nPartyNum = nPartyNum;
	}

	public String getStrCallerNickname() {
		return strCallerNickname;
	}

	public void setStrCallerNickname(String strCallerNickname) {
		this.strCallerNickname = strCallerNickname;
	}

	public String getStrCalledNickname() {
		return strCalledNickname;
	}

	public void setStrCalledNickname(String strCalledNickname) {
		this.strCalledNickname = strCalledNickname;
	}

	public int getnCertType() {
		return nCertType;
	}

	public void setnCertType(int nCertType) {
		this.nCertType = nCertType;
	}

	public String getStrCertCode() {
		return strCertCode;
	}

	public void setStrCertCode(String strCertCode) {
		this.strCertCode = strCertCode;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public String getStrCountryCode() {
		return strCountryCode;
	}

	public void setStrCountryCode(String strCountryCode) {
		this.strCountryCode = strCountryCode;
	}

}
