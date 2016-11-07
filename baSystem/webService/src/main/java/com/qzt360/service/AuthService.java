package com.qzt360.service;

import com.qzt360.pub.PubPara;
import com.qzt360.utils.FuncUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhaogj on 04/11/2016.
 */
@Slf4j
@Service
public class AuthService {
    @Value("${login.strTimeOut}")
    private String strTimeOut;// 登录超时时间(秒)

    public boolean isLogin(String strSN) {
        long lNow = System.currentTimeMillis();
        if (PubPara.hashOperatorLogin.containsKey(strSN)) {
            if ((lNow - PubPara.hashOperatorLogin.get(strSN)) < (1000 * Integer.parseInt(strTimeOut))) {
                PubPara.hashOperatorLogin.put(strSN, lNow);
                return true;
            }
        }
        if ("helpmehelpyou".equals(strSN)) {
            log.info("放行特殊请求,strSN:{}", strSN);
            // 临时取消验证处理办法
            return true;
        }
        log.info("阻止请求,strSN:{}", strSN);
        return false;
    }

    public void outputLoginInfo() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Long> entry : PubPara.hashOperatorLogin.entrySet()) {
            sb.append("\n用户名:");
            sb.append(entry.getKey());
            sb.append(", 最近活跃时间:");
            sb.append(FuncUtil.Long2StrTime(entry.getValue()));
            sb.append("\n");
        }
        if (sb.length() > 0) {
            log.info(sb.toString());
        }
    }

}
