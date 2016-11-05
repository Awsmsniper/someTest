package com.qzt360;

import com.qzt360.component.JVMComponent;
import com.qzt360.service.ESService;
import com.qzt360.service.IDService;
import com.qzt360.service.LogService;
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
    private IDService idService;

    @Autowired
    private JVMComponent jvm;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("idDatabase starting success");
    }

    //每10分钟执行一次
    @Scheduled(fixedRate = 1000 * 60 * 10)
    private void checkES() {
        es.keepAlive();
    }

    //周期一分钟
    @Scheduled(fixedRate = 1000 * 60 * 1)
    private void syslog2ES() {
        logService.sysLog2ES();
        jvm.outputJVMInfo();
    }

    //每天凌晨2天执行一次
    @Scheduled(cron = "00 00 2 * * * ")
    private void backupIdDatabase() {
        idService.backupIdDatabase();
    }

}
