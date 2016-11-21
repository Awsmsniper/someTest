package com.qzt360.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zhaogj on 21/11/2016.
 */
@EnableScheduling
@Slf4j
@Component
public class CronComponent2 {
    //@Scheduled(fixedRate = 1000 * 1 * 1)
    @Scheduled(cron = "0/1 * * * * * ")
    private void doCron1() {
        log.info("in CronComponent2 doCron1 no sleep,time:{}", System.currentTimeMillis() / 1000L);
    }


}
