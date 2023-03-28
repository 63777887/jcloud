package com.jwk.common.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Jiwk
 * @version 0.1.0
 * <p>
 * 常量表
 * @date 2022/6/11
 */
@ConfigurationProperties(prefix = "jwk.rabbitmq")
public class RabbitmqProperties {

	public String addresses;

	public String username;

	public String password;

	public String virtualHost;

	public String getAddresses() {
		return addresses;
	}

	public void setAddresses(String addresses) {
		this.addresses = addresses;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

}
