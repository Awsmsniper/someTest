package com.qzt360.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListHtml {
	private String strResult = "未登录用户不允许查询";
	private long lCount = 0;
	private int nPage = 1;
	private int nLimit = 30;
	private List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

	public String getStrResult() {
		return strResult;
	}

	public void setStrResult(String strResult) {
		this.strResult = strResult;
	}

	public long getlCount() {
		return lCount;
	}

	public void setlCount(long lCount) {
		this.lCount = lCount;
	}

	public int getnPage() {
		return nPage;
	}

	public void setnPage(int nPage) {
		this.nPage = nPage;
	}

	public List<Map<String, Object>> getListMap() {
		return listMap;
	}

	public void setListMap(List<Map<String, Object>> listMap) {
		this.listMap = listMap;
	}

	public int getnLimit() {
		return nLimit;
	}

	public void setnLimit(int nLimit) {
		this.nLimit = nLimit;
	}

}
