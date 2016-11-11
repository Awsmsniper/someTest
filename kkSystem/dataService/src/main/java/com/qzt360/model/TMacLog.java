package com.qzt360.model;

import lombok.Data;

/**
 * Created by zhaogj on 11/11/2016.
 */
@Data
public class TMacLog {
    private String strTmac;                         // 终端MAC地址
    private String strTbrand;                       // 终端品牌
    private String strTssidList;                     // 终端历史SSID列表
    private long lCollectTime;                     // 采集时间
    private String strTfieldIntensity;             // 被采终端场强
    private String strIdType;                         // 身份类型
    private String strIdContent;                     // 身份内容
    private String strApSsid;                         // 热点SSID
    private String strApMac;                         // 热点MAC地址
    private String strApChannel;                   // 热点频道
    private String strApEncType;                   // 热点加密类型
    private String strApX;                               // X坐标
    private String strApY;                               // Y坐标
    private String strPlaceCode;                     // 场所编号
    private String strDeviceCode;                     // 设备编号
    private String strDeviceLongitude;             // 设备经度
    private String strDeviceLatitude;             // 设备纬度
}
