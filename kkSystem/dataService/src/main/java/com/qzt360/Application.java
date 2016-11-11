package com.qzt360;


import com.qzt360.component.JVMComponent;
import com.qzt360.service.ESService;
import com.qzt360.service.Log2ESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Log2ESService log2es;

    public static void main(String[] args) {
        log.info("dataService is starting...");
        SpringApplication.run(Application.class);
        log.info("dataService starting success");
    }

    //每10分钟执行一次
    @Scheduled(fixedRate = 1000 * 60 * 10)
    private void doSomething() {
        es.keepAlive();
        //jvm内存的使用情况打印
        jvm.outputJVMInfo();
        //日志入库es
        log2es.startTMacLog2ES();
    }

}
