package com.qzt360.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于备案查询中展示设备信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqpInfoBA {
    private String strId = "";// 设备ID
    private String strDataStatus = "";// 数据状态
    private String strBAStatus = "";// 备案状态
    private String strBATime = "";// 备案时间

    public EqpInfoBA(String strId) {
        super();
        this.strId = strId;
    }

}
