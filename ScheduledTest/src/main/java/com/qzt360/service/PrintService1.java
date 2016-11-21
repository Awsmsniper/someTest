package com.qzt360.service;

import lombok.extern.slf4j.Slf4j;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.springframework.stereotype.Service;

/**
 * Created by zhaogj on 21/11/2016.
 */
@Service
@Slf4j
public class PrintService1 {
    public void doPrint() {
        while (true) {
            log.info("PrintService1 working sleep 3s time:{}", System.currentTimeMillis());
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
