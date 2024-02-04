package monami.lms.responceentities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigResponce extends BasicResponce {
	String Message;
	String Config_Status;
	
	
	String simultaneousuploads;
	String polltime;
	
	
	String ftpHost;
	String ftpUsername;
	String ftpPassword;
	String ftpPort;
	String serverTime;
	String ftpProtocol;
	
	private String serverDiscovery;
	
	
	
	
	
	public String getFtpHost() {
		return ftpHost;
	}
	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}
	public String getFtpUsername() {
		return ftpUsername;
	}
	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}
	public String getFtpPassword() {
		return ftpPassword;
	}
	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	public String getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(String ftpPort) {
		this.ftpPort = ftpPort;
	}
	@JsonProperty("simultaneousuploads")
	public String getSimultaneousuploads() {
		return simultaneousuploads;
	}
	public void setSimultaneousuploads(String simultaneousuploads) {
		this.simultaneousuploads = simultaneousuploads;
	}
	@JsonProperty("polltime")
	public String getPolltime() {
		return polltime;
	}
	public void setPolltime(String polltime) {
		this.polltime = polltime;
	}
	public ConfigResponce(boolean rq,String message, String config_Status) {
		super(rq);
		Message = message;
		Config_Status = config_Status;
	}
	@JsonProperty("Message")
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	@JsonProperty("Config_Status")
	public String getConfig_Status() {
		return Config_Status;
	}
	public void setConfig_Status(String config_Status) {
		Config_Status = config_Status;
	}
	public String getServerDiscovery() {
		return serverDiscovery;
	}
	public void setServerDiscovery(String serverDiscovery) {
		this.serverDiscovery = serverDiscovery;
	}
	public String getServerTime() {
		return serverTime;
	}
	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}
	public String getFtpProtocol() {
		return ftpProtocol;
	}
	public void setFtpProtocol(String ftpProtocol) {
		this.ftpProtocol = ftpProtocol;
	}
	
	
	
	
}