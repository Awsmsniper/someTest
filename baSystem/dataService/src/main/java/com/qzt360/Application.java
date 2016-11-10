package com.qzt360;


import com.qzt360.service.TestService;
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
    private TestService test;

    @Value("${es.strClusterName}")
    private String strClusterName;


    public static void main(String[] args) {
        log.info("dataService is starting...");
        SpringApplication.run(Application.class);
        log.info("dataService starting success");
    }
    //周期一分钟
    @Scheduled(fixedRate = 1000 * 1 * 1)
    private void syslog2ES() {
        log.info("in scheduled");
        log.info(strClusterName);
        test.doPrint();
    }


}
