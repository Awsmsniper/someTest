package com.qzt360;

import com.qzt360.service.ESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static java.lang.Thread.sleep;

/**
 * Created by zhaogj on 03/11/2016.
 */
@SpringBootApplication
@Slf4j
@EnableScheduling
public class Application {

    @Autowired
    private ESService es;

    public static void main(String[] args) {
        log.info("idDatabase is starting...");
        SpringApplication.run(Application.class, args);
        log.info("idDatabase starting success");
    }

    /**
     * 每半小时执行一次
     */
    @Scheduled(fixedRate = 1000 * 1 * 1)//(cron = "0/1 * * * * * ")
    private void doCron() {
        es.keepAlive();
    }

}
