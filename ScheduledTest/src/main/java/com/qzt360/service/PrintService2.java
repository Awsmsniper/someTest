package com.qzt360.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by zhaogj on 21/11/2016.
 */
@Service
@Slf4j
public class PrintService2 {
    public void doPrint() {
        while (true) {
            log.info("PrintService2 working sleep 1s time:{}", System.currentTimeMillis());
            try {
                Thread.sleep(1000 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
