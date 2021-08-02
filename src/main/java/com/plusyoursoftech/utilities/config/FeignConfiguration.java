package com.plusyoursoftech.utilities.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {
	@Value("${acs.host.proxy}")
	private String acsHostProxy;
	@Value("${acs.host.proxy-port}")
	private String acsHostProxyPort;

	/*
	 * @Bean public Client feignClient() {
	 * 
	 * return new Client.Proxied(null, null, new Proxy(Proxy.Type.HTTP, new
	 * InetSocketAddress(getAcsHostProxy(),
	 * Integer.parseInt(getAcsHostProxyPort()))));
	 * 
	 * }
	 */
	public String getAcsHostProxy() {
		return acsHostProxy;
	}

	public void setAcsHostProxy(String acsHostProxy) {
		this.acsHostProxy = acsHostProxy;
	}

	public String getAcsHostProxyPort() {
		return acsHostProxyPort;
	}

	public void setAcsHostProxyPort(String acsHostProxyPort) {
		this.acsHostProxyPort = acsHostProxyPort;
	}

}