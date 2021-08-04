package com.alfresco.utilities.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Plusyoursoftech
 *
 */
@SpringBootApplication
@EnableFeignClients("com.plusyoursoftech.utilities.client")
@ComponentScan("com.plusyoursoftech.utilities*") // Using a root package also allows the @ComponentScan annotation to be used without needing to specify a basePackage attribute
public class PlusyoursoftechUtilitiesApplication {
	static Logger logger = LoggerFactory.getLogger(PlusyoursoftechUtilitiesApplication.class);

	public static void main(String[] args) {
		logger.info("plusyoursoftech-utilities service is Starting...");
		SpringApplication.run(PlusyoursoftechUtilitiesApplication.class, args);
		logger.info("plusyoursoftech-utilities service is Started");
	}

}
