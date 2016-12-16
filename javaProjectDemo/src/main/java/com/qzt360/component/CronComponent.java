package com.qzt360.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by zhaogj on 17/11/2016.
 * 定时任务
 */
@EnableScheduling
@Component
@Slf4j
public class CronComponent {

    @Autowired
    private JVMComponent jvm;

    //每1分钟执行一次
    @Scheduled(fixedRate = 1000 * 60 * 1)
    private void doSomething() {
        //jvm内存的使用情况打印
        jvm.outputJVMInfo();
    }

}
