package com.plusyoursoftech.utilities.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Plusyoursoftech
 *
 */
@Component
public class ApplicationConfig {
	Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
	@Value("${acs.host.url}")
	private String acsHostUrl;
	@Value("${acs.host.user}")
	private String acsHostUser;

	@Value("${acs.host.pwd}")
	private String acsHostPwd;

	public String getAcsHostUrl() {
		logger.info("ACS Host URL: " + acsHostUrl);
		return acsHostUrl;
	}

	public void setAcsHostUrl(String acsHostUrl) {
		this.acsHostUrl = acsHostUrl;
	}

	public String getAcsHostUser() {
		logger.info("ACS Host user: " + acsHostUser);
		return acsHostUser;
	}

	public void setAcsHostUser(String acsHostUser) {
		this.acsHostUser = acsHostUser;
	}

	public String getAcsHostPwd() {
		return acsHostPwd;
	}

	public void setAcsHostPwd(String acsHostPwd) {
		this.acsHostPwd = acsHostPwd;
	}

}
