package com.qzt360.controller;

import com.qzt360.model.EqpInfo;
import com.qzt360.service.AuthService;
import com.qzt360.service.EqpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = {"http://gdba.qzt360.com:36160", "http://121.14.204.68:36160", "http://172.16.29.29:36160",
        "http://localhost:8080", "http://localhost:8081", "http://127.0.0.1:8080", "http://127.0.0.1:81",
        "http://14.146.224.33:20200", "http://14.146.226.78:20200", "http://113.108.186.130:20200",
        "http://smart.189free.cn:7004", "http://14.146.226.78:7004", "http://113.105.0.68:29160",
        "http://127.0.0.1:8020"})
@RestController
@RequestMapping("/eqp")
public class EqpController {
    @Autowired
    private EqpService em;

    @Autowired
    private AuthService auth;

    @RequestMapping(method = RequestMethod.GET)
    public EqpInfo read(@RequestParam(value = "strSN", defaultValue = "") String strSN,
                        @RequestParam(value = "strEqpId", defaultValue = "") String strEqpId) {
        if (!auth.isLogin(strSN)) {
            return null;
        }
        return em.readEqpInfo(strEqpId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public EqpInfo create(@RequestBody EqpInfo eqpInfo) {
        log.info("in create");
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{id}")
    public void delete(@PathVariable String id) {
        log.info("in delete");
    }

    @RequestMapping(method = RequestMethod.PUT, value = "{id}")
    public EqpInfo update(@PathVariable String id, @RequestBody EqpInfo eqpInfo) {
        if (!auth.isLogin(eqpInfo.getStrSN())) {
            return null;
        }
        if (em.updateEqpInfo(id, eqpInfo)) {
            eqpInfo.setStrSN("");// 不回传session
            return eqpInfo;
        }
        return null;
    }
}
