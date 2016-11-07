package com.qzt360.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by zhaogj on 04/11/2016.
 */
@Slf4j
@Service
public class AuthService {
    public boolean isLogin(String strSN) {
        if ("helpmehelpyou".equals(strSN)) {
            return true;
        }
        log.info("阻止请求,strSN:{}", strSN);
        return false;
    }

}
