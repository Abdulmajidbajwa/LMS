package monami.lms.responceentities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReponceWithMessageAndConfigStatus extends BasicResponce{
	String Message;
	String Config_Status;

	public ReponceWithMessageAndConfigStatus(){

	}
	
	
	@JsonProperty("Config_Status")
	public String getConfig_Status() {
		return Config_Status;
	}



	public void setConfig_Status(String config_Status) {
		Config_Status = config_Status;
	}



	public ReponceWithMessageAndConfigStatus(boolean requestAction, String message,String cs) {
		super(requestAction);
		this.Message=message;
		this.Config_Status=cs;
		// TODO Auto-generated constructor stub
	}
	@JsonProperty("Message")
	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	



}