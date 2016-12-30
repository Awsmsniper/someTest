package com.qzt360.component;

import com.qzt360.repository.MyTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by zhaogj on 17/11/2016.
 * 工程启动时需要加载的动作写在这里
 */
@Component
@Slf4j
@Order(value = 1)
public class StartRunnerComponent implements CommandLineRunner {

    @Autowired
    private MyTestRepository myTestR;

    @Override
    public void run(String... args) throws Exception {
        log.info("start init");
        myTestR.doSomething();
        log.info("end init");
    }
}
