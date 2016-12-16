package com.qzt360.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by zhaogj on 15/12/2016.
 */
@Service
@Slf4j
public class MyTestService {
    public void startJob() {
        while (true) {
            doSomething();
            try {
                Thread.sleep(1000 * 2);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    public void doSomething() {
        log.info("start doing something");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("end doing something");
    }
}
