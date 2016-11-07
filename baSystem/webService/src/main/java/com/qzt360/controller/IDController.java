package com.qzt360.controller;

import com.qzt360.service.AuthService;
import com.qzt360.service.IDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhaogj on 04/11/2016.
 */
@RestController
public class IDController {
    @Autowired
    private IDService id;
    @Autowired
    private AuthService auth;

    // 提交身份组
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://127.0.0.1:81", "http://113.105.0.68:29160"})
    @RequestMapping("/submitIds")
    public String submitIds(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                            @RequestParam(value = "strEqpId", defaultValue = "") String strEqpId,
                            @RequestParam(value = "strIds", defaultValue = "") String strIds) {
        if (auth.isLogin(strSN)) {
            return id.submitIds(strEqpId, strIds);
        } else {
            return "没有权限提交数据";
        }
    }

    // 查询直接关联
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://127.0.0.1:81", "http://113.105.0.68:29160"})
    @RequestMapping("/getIdsDirect")
    public List<String> getIdsDirect(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                                     @RequestParam(value = "strId", defaultValue = "") String strId) {
        if (auth.isLogin(strSN)) {
            return id.getIdsDirect(strId);
        } else {
            return null;
        }
    }

    // 查询智能关联
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://127.0.0.1:81", "http://113.105.0.68:29160"})
    @RequestMapping("/getIdsSmart")
    public List<String> getIdsSmart(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                                    @RequestParam(value = "strId", defaultValue = "") String strId) {
        if (auth.isLogin(strSN)) {
            return id.getIdsSmart(strId);
        } else {
            return null;
        }
    }

    // 身份查询，模糊匹配
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://127.0.0.1:81", "http://113.105.0.68:29160"})
    @RequestMapping("/getIds")
    public List<String> getIds(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                               @RequestParam(value = "strId", defaultValue = "") String strId) {
        if (auth.isLogin(strSN)) {
            return id.getIds(strId);
        } else {
            return null;
        }
    }
}
