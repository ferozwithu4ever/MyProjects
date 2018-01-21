/**
 * 
 */
package com.vo;

/**
 * @author User
 *
 */
public class SmtpVO {

	private String serviceProvider;
	private String host;
	private String hostValue;
	private String auth;
	private boolean authValue;
	private String startTls;
	private boolean startTlsValue;
	private String port;
	private String portValue;
	
	
	public String getPortValue() {
		return portValue;
	}
	public void setPortValue(String portValue) {
		this.portValue = portValue;
	}
	public String getServiceProvider() {
		return serviceProvider;
	}
	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHostValue() {
		return hostValue;
	}
	public void setHostValue(String hostValue) {
		this.hostValue = hostValue;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public boolean isAuthValue() {
		return authValue;
	}
	public void setAuthValue(boolean authValue) {
		this.authValue = authValue;
	}
	public String getStartTls() {
		return startTls;
	}
	public void setStartTls(String startTls) {
		this.startTls = startTls;
	}
	public boolean isStartTlsValue() {
		return startTlsValue;
	}
	public void setStartTlsValue(boolean startTlsValue) {
		this.startTlsValue = startTlsValue;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
}
