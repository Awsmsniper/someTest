package com.qzt360.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhaogj on 04/11/2016.
 */
@RestController
public class IDQueryController {
    // 提交身份组
    @CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
            "http://localhost:8081", "http://127.0.0.1:8020", "http://127.0.0.1:81", "http://113.105.0.68:29160"})
    @RequestMapping("/submitIds")
    public String submitIds(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                            @RequestParam(value = "strEqpId", defaultValue = "") String strEqpId,
                            @RequestParam(value = "strIds", defaultValue = "") String strIds) {
        return "没有权限提交数据";
    }
}
