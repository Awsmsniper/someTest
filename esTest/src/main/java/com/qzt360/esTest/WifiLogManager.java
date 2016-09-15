package com.qzt360.esTest;

import org.apache.log4j.Logger;

public class WifiLogManager {
	private static Logger logger = Logger.getLogger(WifiLogManager.class);
	private String strTMac;// 0
	private String strTBrand;
	private String strTSsidList;
	private int nCollectTime;
	private String strTFieldIntensity;
	private int nIdType;// 5
	private String strIdCode;
	private String strApSsid;
	private String strApMac;
	private String strApChannel;
	private String strApEncType;// 10
	private String strApX;
	private String strApY;
	private String strPlaceCode;
	private String strDeviceCode;
	private String strDeviceLongitude;// 15
	private String strDeviceLatitude;

	public boolean isLegal(String strLine) {
		String[] astrLine = (strLine + "\tmark").split("\t");
		if (astrLine.length == 18) {
			try {
				strTMac = astrLine[0];// 0
				strTBrand = astrLine[1];
				strTSsidList = astrLine[2];
				nCollectTime = Integer.parseInt(astrLine[3]);
				strTFieldIntensity = astrLine[4];
				nIdType = Integer.parseInt(astrLine[5]);// 5
				strIdCode = astrLine[6];
				strApSsid = astrLine[7];
				strApMac = astrLine[8];
				strApChannel = astrLine[9];
				strApEncType = astrLine[10];// 10
				strApX = astrLine[11];
				strApY = astrLine[12];
				strPlaceCode = astrLine[13];
				strDeviceCode = astrLine[14];
				strDeviceLongitude = astrLine[15];// 15
				strDeviceLatitude = astrLine[16];
			} catch (Exception e) {
				logger.error(e);
				return false;
			}
			return true;
		}
		return false;
	}

	public String getStrTMac() {
		return strTMac;
	}

	public void setStrTMac(String strTMac) {
		this.strTMac = strTMac;
	}

	public String getStrTBrand() {
		return strTBrand;
	}

	public void setStrTBrand(String strTBrand) {
		this.strTBrand = strTBrand;
	}

	public String getStrTSsidList() {
		return strTSsidList;
	}

	public void setStrTSsidList(String strTSsidList) {
		this.strTSsidList = strTSsidList;
	}

	public int getnCollectTime() {
		return nCollectTime;
	}

	public void setnCollectTime(int nCollectTime) {
		this.nCollectTime = nCollectTime;
	}

	public String getStrTFieldIntensity() {
		return strTFieldIntensity;
	}

	public void setStrTFieldIntensity(String strTFieldIntensity) {
		this.strTFieldIntensity = strTFieldIntensity;
	}

	public int getnIdType() {
		return nIdType;
	}

	public void setnIdType(int nIdType) {
		this.nIdType = nIdType;
	}

	public String getStrIdCode() {
		return strIdCode;
	}

	public void setStrIdCode(String strIdCode) {
		this.strIdCode = strIdCode;
	}

	public String getStrApSsid() {
		return strApSsid;
	}

	public void setStrApSsid(String strApSsid) {
		this.strApSsid = strApSsid;
	}

	public String getStrApMac() {
		return strApMac;
	}

	public void setStrApMac(String strApMac) {
		this.strApMac = strApMac;
	}

	public String getStrApChannel() {
		return strApChannel;
	}

	public void setStrApChannel(String strApChannel) {
		this.strApChannel = strApChannel;
	}

	public String getStrApEncType() {
		return strApEncType;
	}

	public void setStrApEncType(String strApEncType) {
		this.strApEncType = strApEncType;
	}

	public String getStrApX() {
		return strApX;
	}

	public void setStrApX(String strApX) {
		this.strApX = strApX;
	}

	public String getStrApY() {
		return strApY;
	}

	public void setStrApY(String strApY) {
		this.strApY = strApY;
	}

	public String getStrPlaceCode() {
		return strPlaceCode;
	}

	public void setStrPlaceCode(String strPlaceCode) {
		this.strPlaceCode = strPlaceCode;
	}

	public String getStrDeviceCode() {
		return strDeviceCode;
	}

	public void setStrDeviceCode(String strDeviceCode) {
		this.strDeviceCode = strDeviceCode;
	}

	public String getStrDeviceLongitude() {
		return strDeviceLongitude;
	}

	public void setStrDeviceLongitude(String strDeviceLongitude) {
		this.strDeviceLongitude = strDeviceLongitude;
	}

	public String getStrDeviceLatitude() {
		return strDeviceLatitude;
	}

	public void setStrDeviceLatitude(String strDeviceLatitude) {
		this.strDeviceLatitude = strDeviceLatitude;
	}

}
