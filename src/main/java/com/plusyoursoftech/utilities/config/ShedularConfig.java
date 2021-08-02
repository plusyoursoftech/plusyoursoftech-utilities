package com.plusyoursoftech.utilities.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.plusyoursoftech.utilities.service.AlfrescoContentService;

/**
 * @author Plusyoursoftech
 *
 */
@Configuration
@EnableScheduling
public class ShedularConfig {
	Logger logger = LoggerFactory.getLogger(ShedularConfig.class);
	@Value("${shedule.cron.enable}")
	private boolean isEnable;

	@Autowired
	private ApplicationConfig applicationConfig;
	@Autowired
	private AlfrescoContentService alfrescoContentService;

	@Scheduled(cron = "${shedule.cron.expression}")
	public void scheduleTaskUsingCronExpression() {

		if (isEnable) {
			DateFormat dataFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
			Date result = new Date(System.currentTimeMillis());
			logger.info("Shedular Started at -> " + dataFormat.format(result));

			try {

			} catch (Exception e) {
				logger.error("Error", e);
			}

			result = new Date(System.currentTimeMillis());
			logger.info("Shedular Finished at -> " + dataFormat.format(result));
		}
	}

}
