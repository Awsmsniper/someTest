package com.qzt360.controller;

import com.qzt360.model.BAInfo;
import com.qzt360.model.DataStatus;
import com.qzt360.model.ListHtml;
import com.qzt360.model.OperatorInfo;
import com.qzt360.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BAController {
    @Autowired
    private AuthService auth;
    @Autowired
    private BAService ba;
    @Autowired
    private EqpService eqp;
    @Autowired
    private NetLogService netLog;
    @Autowired
    private AuthLogService authLog;
    @Autowired
    private NetIdLogService netIdLog;
    @Autowired
    private OperatorService operator;
    @Autowired
    private DataStatusService dataStatus;

    // 备案信息查询
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8080", "http://127.0.0.1:8080", "http://127.0.0.1:81", "http://14.146.224.33:20200",
            "http://14.146.226.78:20200", "http://113.108.186.130:20200", "http://smart.189free.cn:7004",
            "http://14.146.226.78:7004", "http://113.105.0.68:29160"})
    @RequestMapping("/baInfo")
    public BAInfo getBAInfo(@RequestParam(value = "strId", defaultValue = "") String strId) {
        BAInfo baInfo = ba.getBAInfoById(strId);
        return baInfo;
    }

    // 设备信息列表查询
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://localhost:8080", "http://127.0.0.1:81",
            "http://113.105.0.68:29160"})
    @RequestMapping("/eqpList")
    public ListHtml getEqpList(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                               @RequestParam(value = "strBusinessLicense", defaultValue = "") String strBusinessLicense,
                               @RequestParam(value = "strUnitName", defaultValue = "") String strUnitName,
                               @RequestParam(value = "strUnitType", defaultValue = "") String strUnitType,
                               @RequestParam(value = "strUnitZone", defaultValue = "") String strUnitZone,
                               @RequestParam(value = "strFeedback", defaultValue = "") String strFeedback,
                               @RequestParam(value = "strEqpId", defaultValue = "") String strEqpId,
                               @RequestParam(value = "strBAStatus", defaultValue = "") String strBAStatus,
                               @RequestParam(value = "strConfirmStatus", defaultValue = "") String strConfirmStatus,
                               @RequestParam(value = "strBeginTime", defaultValue = "") String strBeginTime,
                               @RequestParam(value = "strEndTime", defaultValue = "") String strEndTime,
                               @RequestParam(value = "nPage", defaultValue = "1") int nPage,
                               @RequestParam(value = "nLimit", defaultValue = "30") int nLimit) {
        if (!auth.isLogin(strSN)) {
            return (new ListHtml());
        } else {
            if (nLimit > 200) {
                nLimit = 30;
            }
            return eqp.getEqpList(strBusinessLicense, strUnitName, strUnitType, strUnitZone, strFeedback, strEqpId,
                    strBAStatus, strConfirmStatus, strBeginTime, strEndTime, nPage, nLimit);
        }
    }

    // 管理员登录
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://localhost:8080", "http://127.0.0.1:8020", "http://127.0.0.1:81",
            "http://113.105.0.68:29160"})
    @RequestMapping("/login")
    public OperatorInfo login(@RequestParam(value = "strUserName", defaultValue = "") String strUserName,
                              @RequestParam(value = "strPassword", defaultValue = "") String strPassword) {
        return operator.login(strUserName, strPassword);
    }

    // 管理员退出
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://localhost:8080", "http://127.0.0.1:8020", "http://127.0.0.1:81",
            "http://113.105.0.68:29160"})
    @RequestMapping("/logout")
    public String logout(@RequestParam(value = "strSN", defaultValue = "") String strSN) {
        operator.logout(strSN);
        return "success";
    }

    // 审计日志查询
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://localhost:8080", "http://127.0.0.1:81",
            "http://113.105.0.68:29160"})
    @RequestMapping("/netLogList")
    public ListHtml getNetLog(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                              @RequestParam(value = "strUnitCode", defaultValue = "") String strUnitCode,
                              @RequestParam(value = "strCollectionEquipmentId", defaultValue = "") String strCollectionEquipmentId,
                              @RequestParam(value = "strAPMac", defaultValue = "") String strAPMac,
                              @RequestParam(value = "strMac", defaultValue = "") String strMac,
                              @RequestParam(value = "strBeginTime", defaultValue = "") String strBeginTime,
                              @RequestParam(value = "strEndTime", defaultValue = "") String strEndTime,
                              @RequestParam(value = "nPage", defaultValue = "1") int nPage,
                              @RequestParam(value = "nLimit", defaultValue = "30") int nLimit) {
        if (!auth.isLogin(strSN)) {
            return (new ListHtml());
        } else {
            if (nLimit > 200) {
                nLimit = 30;
            }
            return netLog.getNetLog(strUnitCode, strCollectionEquipmentId, strAPMac, strMac, strBeginTime, strEndTime,
                    nPage, nLimit);
        }
    }

    // 终端上下线日志查询
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://localhost:8080", "http://127.0.0.1:81",
            "http://113.105.0.68:29160"})
    @RequestMapping("/authLogList")
    public ListHtml getAuthLog(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                               @RequestParam(value = "strUnitCode", defaultValue = "") String strUnitCode,
                               @RequestParam(value = "strCollectionEquipmentId", defaultValue = "") String strCollectionEquipmentId,
                               @RequestParam(value = "strAPMac", defaultValue = "") String strAPMac,
                               @RequestParam(value = "strMac", defaultValue = "") String strMac,
                               @RequestParam(value = "strAccount", defaultValue = "") String strAccount,
                               @RequestParam(value = "strBeginTime", defaultValue = "") String strBeginTime,
                               @RequestParam(value = "strEndTime", defaultValue = "") String strEndTime,
                               @RequestParam(value = "nPage", defaultValue = "1") int nPage,
                               @RequestParam(value = "nLimit", defaultValue = "30") int nLimit) {
        if (!auth.isLogin(strSN)) {
            return (new ListHtml());
        } else {
            if (nLimit > 200) {
                nLimit = 30;
            }
            return authLog.getAuthLog(strUnitCode, strCollectionEquipmentId, strAPMac, strMac, strAccount, strBeginTime,
                    strEndTime, nPage, nLimit);
        }
    }

    // 虚拟身份查询
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://localhost:8080", "http://127.0.0.1:81",
            "http://113.105.0.68:29160"})
    @RequestMapping("/netIdLogList")
    public ListHtml getNetIdLog(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                                @RequestParam(value = "strUnitCode", defaultValue = "") String strUnitCode,
                                @RequestParam(value = "strAPMac", defaultValue = "") String strAPMac,
                                @RequestParam(value = "strNetId", defaultValue = "") String strNetId,
                                @RequestParam(value = "strBeginTime", defaultValue = "") String strBeginTime,
                                @RequestParam(value = "strEndTime", defaultValue = "") String strEndTime,
                                @RequestParam(value = "nPage", defaultValue = "1") int nPage,
                                @RequestParam(value = "nLimit", defaultValue = "30") int nLimit) {
        if (!auth.isLogin(strSN)) {
            return (new ListHtml());
        } else {
            if (nLimit > 200) {
                nLimit = 30;
            }
            return netIdLog.getNetIdLog(strUnitCode, strAPMac, strNetId, strBeginTime, strEndTime, nPage, nLimit);
        }

    }

    // 数据量统计
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://localhost:8080", "http://127.0.0.1:81",
            "http://113.105.0.68:29160"})
    @RequestMapping("/dataStatus")
    public DataStatus getDataStatus(@RequestParam(value = "strSN", defaultValue = "") String strSN) {
        if (auth.isLogin(strSN)) {
            return dataStatus.getDataStatus();
        } else {
            return null;
        }

    }
}
