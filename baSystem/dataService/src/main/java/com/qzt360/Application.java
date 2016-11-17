package com.qzt360;


import com.qzt360.component.JVMComponent;
import com.qzt360.service.ESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class Application {
    @Autowired
    private ESService es;

    @Autowired
    private JVMComponent jvm;

    public static void main(String[] args) {
        log.info("dataService is starting...");

        SpringApplication.run(Application.class);
        log.info("dataService starting success");
    }

    //每10分钟执行一次
    @Scheduled(fixedRate = 1000 * 60 * 10)
    private void checkES() {
        es.keepAlive();
    }

    @Scheduled(fixedRate = 1000 * 3 * 1)
    private void do3Second() {
        //jvm内存的使用情况打印
        jvm.outputJVMInfo();
    }

}
