package com.qzt360;

import com.qzt360.repository.ESRepository;
import com.qzt360.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ESRepository es;

    @Autowired
    private TestService test;

    @Value("${es.strClusterName}")
    private String strClusterName;

    public static void main(String[] args) {
        log.info("idDatabase is starting...");
        SpringApplication.run(Application.class, args);
//        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
//        String[] astrBeanName = ctx.getBeanDefinitionNames();
//        Arrays.sort(astrBeanName);
//        for (String strBeanName : astrBeanName) {
//            log.info(strBeanName);
//        }

        log.info("idDatabase starting success");
    }


    /**
     * 每1秒执行一次
     */
    @Scheduled(fixedRate = 1000)//(cron = "0/1 * * * * * ")
    private void doCron() {
        log.info("begin cron");
        log.info("strClusterName:{}", strClusterName);
        es.nCount++;
        log.info("cron read count:{}", es.nCount);
        test.doTest();
        log.info("end cron");
    }

    /**
     * 每1秒执行一次
     */
//    @Scheduled(fixedRate = 1000)
//    private void doFixedRate() {
//        log.info("begin fixedRate");
//        es.nCount++;
//        log.info("fixedRate read count:{}", es.nCount);
//        log.info("end fixedRate");
//        log.info("sleep 10s");
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

}
