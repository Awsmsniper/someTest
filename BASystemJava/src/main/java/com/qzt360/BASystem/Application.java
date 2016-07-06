package com.qzt360.BASystem;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
	private static Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) {
		logger.debug("Application start");
		SpringApplication.run(Application.class, args);
	}
}
