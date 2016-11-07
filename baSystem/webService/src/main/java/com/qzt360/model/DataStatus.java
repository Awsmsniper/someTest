package com.qzt360.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataStatus {
	private long lAWifiAuthlogCount;// 终端上下线条数
	private long lAWifiNetlogCount;// 审计日志条数
	private long lAWifiNetIdlogCount;// 虚拟身份日志条数
	private long lAWifiEqpCount;// aWifi设备个数
	private long lIdCount;// 身份个数
}
