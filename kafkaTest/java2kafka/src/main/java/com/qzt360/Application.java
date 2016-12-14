package com.qzt360;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        log.info("dataService is starting...");
        SpringApplication.run(Application.class);
        log.info("dataService starting success");
    }

}
