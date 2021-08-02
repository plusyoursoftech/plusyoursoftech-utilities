package com.plusyoursoftech.utilities;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.plusyoursoftech.utilities.config.ApplicationConfig;

/**
 * @author plusyoursoftech-utilities
 *
 */
@Component
public class AuthenticationUtils {

	private String acsLogin;
	private String sscAcsLogin;
	@Autowired
	private ApplicationConfig applicationConfig;

	@PostConstruct
	private void buildBasicAuth() {
		String authString = applicationConfig.getAcsHostUser() + ":" + decodeString(applicationConfig.getAcsHostPwd());
		byte[] authEncBytes = Base64Utils.encode(authString.getBytes());
		this.acsLogin = "Basic " + new String(authEncBytes);
	}

	/**
	 * @param encodeValue
	 * @return
	 */
	public static String decodeString(String encodeValue) {
		return new String(Base64Utils.decodeFromString(encodeValue));
	}

	public String getAcsLogin() {
		return acsLogin;
	}

	public String getAcsSscLogin() {
		return sscAcsLogin;
	}

}
