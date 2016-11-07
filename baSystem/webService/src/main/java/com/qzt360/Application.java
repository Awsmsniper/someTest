package com.qzt360;

import com.qzt360.component.JVMComponent;
import com.qzt360.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


/**
 * Created by zhaogj on 03/11/2016.
 */
@SpringBootApplication
@Slf4j
@EnableScheduling
public class Application {

    @Autowired
    private ESService es;
    @Autowired
    private LogService logService;
    @Autowired
    private JVMComponent jvm;
    @Autowired
    private AuthService auth;
    @Autowired
    private OperatorService operator;
    @Autowired
    private DataStatusService dataStatus;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("BAWebService starting success");
    }

    //每10分钟执行一次
    @Scheduled(fixedRate = 1000 * 60 * 10)
    private void checkES() {
        es.keepAlive();
    }

    //周期一分钟
    @Scheduled(fixedRate = 1000 * 60 * 1)
    private void doPerMinute() {
        //请求产生的log入库es
        logService.sysLog2ES();
        //登陆用户信息打印
        auth.outputLoginInfo();
        //清理超时的登陆session
        operator.cleanLoginSession();
    }

    @Scheduled(fixedRate = 1000 * 3 * 1)
    private void do3Second() {
        //jvm内存的使用情况打印
        jvm.outputJVMInfo();
    }
}
